package edu.sustech.interaction.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.api.client.CourseClient;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.VideoDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CommentException;
import edu.sustech.common.util.UserContext;
import edu.sustech.interaction.entity.VideoComment;
import edu.sustech.interaction.entity.dto.CommentDTO;
import edu.sustech.interaction.entity.vo.CommentTreeVO;
import edu.sustech.interaction.mapper.VideoCommentMapper;
import edu.sustech.interaction.service.VideoCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<VideoComment> rootComments = baseMapper.selectList(
                new QueryWrapper<VideoComment>()
                        .eq("video_id", vid)
                        .eq("root_id", 0)
                        .orderByDesc(type == 1 ? "(love - bad)" : "gmt_create")
                        .last("LIMIT 10 OFFSET " + offset)
        );

        List<CommentTreeVO> commmentTreeVOList = rootComments.stream().parallel()
                .map(rootComment -> {
                    return getCommentTree(rootComment, 2L);
                })
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
        videoComment.setUserId(userId).setLove(0L).setBad(0L).setIsTop((byte) 0);
        baseMapper.insert(videoComment);

        // 更新评论数量
        // TODO 改为消息队列异步更新评论数量
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
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommentException(MessageConstant.NOT_LOGIN);
        }
        VideoComment videoComment = baseMapper.selectById(id);
        if (videoComment == null) {
            throw new CommentException(MessageConstant.COMMENT_NOT_EXIST);
        }

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


    private CommentTreeVO getCommentTree(VideoComment rootComment, Long limit) {
        CommentTreeVO commentTreeVO = BeanUtil.copyProperties(rootComment, CommentTreeVO.class);
        commentTreeVO.setCount(baseMapper.selectCount(
                new LambdaQueryWrapper<VideoComment>()
                        .eq(VideoComment::getRootId, rootComment.getId())
        ));
        commentTreeVO.setUser(userClient.getUserAndCoursesById(rootComment.getUserId()).getData());
        commentTreeVO.setToUser(userClient.getUserAndCoursesById(rootComment.getToUserId()).getData());

        if (rootComment.getRootId() == 0) {
            QueryWrapper<VideoComment> queryWrapper = new QueryWrapper<VideoComment>()
                    .eq("root_id", rootComment.getId())
                    .orderByDesc("love - bad");
            if (limit > 0) {
                queryWrapper.last("LIMIT " + limit);
            }
            List<VideoComment> childComments = baseMapper.selectList(queryWrapper);
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
}
