package edu.sustech.interaction.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.client.CourseClient;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.VideoDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CommentException;
import edu.sustech.common.util.UserContext;
import edu.sustech.interaction.entity.VideoComment;
import edu.sustech.interaction.entity.VideoCommentLike;
import edu.sustech.interaction.entity.dto.CommentDTO;
import edu.sustech.interaction.entity.enums.VideoCommentLikeStatus;
import edu.sustech.interaction.entity.vo.CommentTreeVO;
import edu.sustech.interaction.entity.vo.VideoCommentLikeVO;
import edu.sustech.interaction.mapper.VideoCommentLikeMapper;
import edu.sustech.interaction.mapper.VideoCommentMapper;
import edu.sustech.interaction.service.VideoCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment> implements VideoCommentService {

    private final VideoCommentLikeMapper videoCommentLikeMapper;

    private final UserClient userClient;

    private final CourseClient courseClient;

    /**
     * 获取对应视频的评论树
     *
     * @param vid    视频id
     * @param offset 已经获取的评论树数量
     * @param type   排序类型 1热度 2时间
     * @return 评论树列表
     */
    @Override
    public Map<String, Object> getCommentTree(Long vid, Long offset, Integer type) {

        // TODO: redis优化查询效率


        Long rootCommentCount = baseMapper.selectCount(
                new LambdaQueryWrapper<VideoComment>()
                        .eq(VideoComment::getVideoId, vid)
                        .eq(VideoComment::getRootId, 0)
        );

        // 获取根评论列表
        List<VideoComment> rootComments = baseMapper.selectComments(vid, 0L, 10L, offset);
        if (type == 1) {
            rootComments.sort(Comparator.comparingLong(comment -> comment.getDislikeCount() - comment.getLikeCount()));
        } else {
            rootComments.sort(Comparator.comparing(VideoComment::getGmtCreate).reversed());
        }

        List<CommentTreeVO> commmentTreeVOList = rootComments.stream().parallel()
                .map(rootComment -> getCommentTree(rootComment, 2L))
                .toList();

        return Map.of(
                "commentTree", commmentTreeVOList,
                "more", rootCommentCount > offset + 10
        );
    }

    /**
     * 获取指定id根评论的评论树
     *
     * @param id 根评论id
     * @return 单棵评论树
     */
    @Override
    public CommentTreeVO getCommentTreeById(Long id) {
        return getCommentTree(baseMapper.selectById(id), -1L);
    }

    /**
     * 新增评论
     *
     * @param commentDTO 评论表单
     * @return 单棵评论树
     */
    @Override
    public CommentTreeVO saveComment(CommentDTO commentDTO) {
        VideoComment videoComment = VideoComment.builder()
                .videoId(commentDTO.getVid())
                .rootId(commentDTO.getRootId())
                .parentId(commentDTO.getParentId())
                .toUserId(commentDTO.getToUserId())
                .content(commentDTO.getContent())
                .build();

        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommentException(MessageConstant.NOT_LOGIN);
        }
        videoComment.setUserId(userId).setLikeCount(0L).setDislikeCount(0L).setIsTop((byte) 0);
        baseMapper.insert(videoComment);

        // 更新评论数量
        courseClient.updateCommentCount(commentDTO.getVid(), 1);

        return getCommentTree(videoComment, -1L);

    }

    /**
     * 删除评论
     *
     * @param id 评论id
     */
    @Transactional
    @Override
    public void deleteComment(Long id) {
        VideoComment videoComment = checkUserAndComment(id);
        Long userId = UserContext.getUser();

        VideoDTO videoDTO = courseClient.getVideoById(videoComment.getVideoId()).getData();
        if (!videoComment.getUserId().equals(userId) && !Objects.equals(videoDTO.getUserId(), userId)) {
            throw new CommentException(MessageConstant.COMMENT_NO_PERMISSION);
        }


        Integer count;
        if (videoComment.getRootId() == 0) {
            // 删除根评论及其子评论
            count = baseMapper.deleteById(id);
            count += baseMapper.delete(
                    new LambdaQueryWrapper<VideoComment>()
                            .eq(VideoComment::getRootId, id)
            );
        } else {
            // 删除该评论并且递归删除子评论
            List<Long> ids = baseMapper.selectIdsToDeleteRecursively(id);
            count = baseMapper.deleteBatchByIds(ids);
        }

        if (count == 0) {
            throw new CommentException(MessageConstant.COMMENT_DELETE_ERROR);
        }

        // 改为消息队列异步更新评论数量
        courseClient.updateCommentCount(videoDTO.getId(), -count);

        log.info("删除评论{}及其{}条子评论", id, count);
    }

    /**
     * 点赞或点踩
     *
     * @param id     评论id
     * @param isLike 是否点赞
     */
    @Override
    @Transactional
    public void likeOrNot(Long id, Boolean isLike) {
        VideoComment videoComment = checkUserAndComment(id);
        Long userId = UserContext.getUser();

        // 查询是否有点赞记录
        LambdaQueryWrapper<VideoCommentLike> queryWrapper = new LambdaQueryWrapper<VideoCommentLike>()
                .eq(VideoCommentLike::getUserId, userId)
                .eq(VideoCommentLike::getCommentId, id);
        VideoCommentLike videoCommentLike = videoCommentLikeMapper.selectOne(queryWrapper);
        // 无记录则插入空记录
        if (videoCommentLike == null) {
            videoCommentLike = VideoCommentLike.builder()
                    .videoId(videoComment.getVideoId())
                    .commentId(id)
                    .userId(userId)
                    .build();
            int insert = videoCommentLikeMapper.insert(videoCommentLike);
            if (insert == 0) {
                throw new CommentException(MessageConstant.LOVE_FAILED);
            } else {
                videoCommentLike = videoCommentLikeMapper.selectOne(queryWrapper);
            }
        }

        if (isLike) {
            if (videoCommentLike.getLikeStatus() == VideoCommentLikeStatus.LIKE) {
                baseMapper.updateLikeCountAndDislikeCount(id, -1, 0);
                videoCommentLike.setLikeStatus(VideoCommentLikeStatus.NONE);
            } else {
                baseMapper.updateLikeCountAndDislikeCount(id, 1, videoCommentLike.getLikeStatus() == VideoCommentLikeStatus.DISLIKE ? -1 : 0);
                videoCommentLike.setLikeStatus(VideoCommentLikeStatus.LIKE);
            }
        } else {
            if (videoCommentLike.getLikeStatus() == VideoCommentLikeStatus.DISLIKE) {
                baseMapper.updateLikeCountAndDislikeCount(id, 0, -1);
                videoCommentLike.setLikeStatus(VideoCommentLikeStatus.NONE);
            } else {
                baseMapper.updateLikeCountAndDislikeCount(id, videoCommentLike.getLikeStatus() == VideoCommentLikeStatus.LIKE ? -1 : 0, 1);
                videoCommentLike.setLikeStatus(VideoCommentLikeStatus.DISLIKE);
            }
        }
        int update = videoCommentLikeMapper.updateById(videoCommentLike);
        if (update == 0) {
            throw new CommentException(MessageConstant.LOVE_FAILED);
        }
    }

    /**
     * 获取用户的点赞点踩记录列表(ID和点赞点踩状态)
     *
     * @return 点赞点踩记录
     */
    @Override
    public List<VideoCommentLikeVO> listLikeOrDislikeRecord() {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommentException(MessageConstant.NOT_LOGIN);
        }
        List<VideoCommentLike> videoCommentLikeList = videoCommentLikeMapper.selectList(
                new LambdaQueryWrapper<VideoCommentLike>()
                        .eq(VideoCommentLike::getUserId, userId)
                        .ne(VideoCommentLike::getLikeStatus, VideoCommentLikeStatus.NONE)
        );
        if (CollUtil.isEmpty(videoCommentLikeList)) {
            return List.of();
        }
        return BeanUtil.copyToList(videoCommentLikeList, VideoCommentLikeVO.class);
    }


    private CommentTreeVO getCommentTree(VideoComment rootComment, Long limit) {
        CommentTreeVO commentTreeVO = BeanUtil.copyProperties(rootComment, CommentTreeVO.class);
        commentTreeVO.setCount(baseMapper.selectCount(
                new LambdaQueryWrapper<VideoComment>()
                        .eq(VideoComment::getRootId, rootComment.getId())
        ));
        commentTreeVO.setUser(userClient.getUserAndCoursesById(rootComment.getUserId()).getData());
        commentTreeVO.setToUser(userClient.getUserAndCoursesById(rootComment.getToUserId()).getData());

        if (rootComment.getRootId() == 0) {
            List<VideoComment> childComments = baseMapper.selectComments(null, rootComment.getId(), limit, 0L);
            childComments.sort(Comparator.comparingLong(comment -> comment.getDislikeCount() - comment.getLikeCount()));

            commentTreeVO.setReplies(
                    childComments.stream().parallel()
                            .map(childComment -> {
                                CommentTreeVO childCommentVO = BeanUtil.copyProperties(childComment, CommentTreeVO.class);
                                childCommentVO.setUser(userClient.getUserAndCoursesById(childComment.getUserId()).getData());
                                childCommentVO.setToUser(userClient.getUserAndCoursesById(childComment.getToUserId()).getData());
                                return childCommentVO;
                            })
                            .toList()
            );
        }
        return commentTreeVO;
    }

    private VideoComment checkUserAndComment(Long commentId) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommentException(MessageConstant.NOT_LOGIN);
        }
        VideoComment videoComment = baseMapper.selectById(commentId);
        if (videoComment == null) {
            throw new CommentException(MessageConstant.COMMENT_NOT_EXIST);
        }
        return videoComment;
    }
}
