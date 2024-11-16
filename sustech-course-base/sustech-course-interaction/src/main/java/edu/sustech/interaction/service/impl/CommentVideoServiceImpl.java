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
import edu.sustech.interaction.entity.CommentVideo;
import edu.sustech.interaction.entity.dto.CommentDTO;
import edu.sustech.interaction.entity.vo.CommentTreeVO;
import edu.sustech.interaction.mapper.CommentVideoMapper;
import edu.sustech.interaction.service.CommentVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class CommentVideoServiceImpl extends ServiceImpl<CommentVideoMapper, CommentVideo> implements CommentVideoService {

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
                new LambdaQueryWrapper<CommentVideo>()
                        .eq(CommentVideo::getVideoId, vid)
                        .eq(CommentVideo::getRootId, 0)
        );

        // 获取根评论列表
        List<CommentVideo> rootComments = baseMapper.selectList(
                new QueryWrapper<CommentVideo>()
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
        CommentVideo commentVideo = CommentVideo.builder()
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
        commentVideo.setUserId(userId).setLove(0L).setBad(0L).setIsTop((byte) 0);
        baseMapper.insert(commentVideo);

        // 更新评论数量
        courseClient.updateCommentCount(commentDTO.getVid(), 1);

        return getCommentTree(commentVideo, -1L);

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
        CommentVideo commentVideo = baseMapper.selectById(id);
        if (commentVideo == null) {
            throw new CommentException(MessageConstant.COMMENT_NOT_EXIST);
        }

        VideoDTO videoDTO = courseClient.getVideoById(commentVideo.getVideoId()).getData();
        if (!commentVideo.getUserId().equals(userId) && !Objects.equals(videoDTO.getUserId(), userId)) {
            throw new CommentException(MessageConstant.COMMENT_NO_PERMISSION);
        }


        Integer count;
        if (commentVideo.getRootId() == 0) {
            // 删除根评论及其子评论
            count = baseMapper.deleteById(id);
            count += baseMapper.delete(
                    new LambdaQueryWrapper<CommentVideo>()
                            .eq(CommentVideo::getRootId, id)
            );
        } else {
            // 删除该评论并且递归删除子评论
            List<Long> ids = baseMapper.selectIdsToDeleteRecursively(id);
            count = baseMapper.deleteBatchByIds(ids);
        }

        if (count == 0) {
            throw new CommentException(MessageConstant.COMMENT_DELETE_ERROR);
        }
        courseClient.updateCommentCount(videoDTO.getId(), -count);

        log.info("删除评论{}及其{}条子评论", id, count);
    }


    private CommentTreeVO getCommentTree(CommentVideo rootComment, Long limit) {
        CommentTreeVO commentTreeVO = BeanUtil.copyProperties(rootComment, CommentTreeVO.class);
        commentTreeVO.setCount(baseMapper.selectCount(
                new LambdaQueryWrapper<CommentVideo>()
                        .eq(CommentVideo::getRootId, rootComment.getId())
        ));
        commentTreeVO.setUser(userClient.getUserAndCoursesById(rootComment.getUserId()).getData());
        commentTreeVO.setToUser(userClient.getUserAndCoursesById(rootComment.getToUserId()).getData());

        if (rootComment.getRootId() == 0) {
            QueryWrapper<CommentVideo> queryWrapper = new QueryWrapper<CommentVideo>()
                    .eq("root_id", rootComment.getId())
                    .orderByDesc("love - bad");
            if (limit > 0) {
                queryWrapper.last("LIMIT " + limit);
            }
            List<CommentVideo> childComments = baseMapper.selectList(queryWrapper);
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
