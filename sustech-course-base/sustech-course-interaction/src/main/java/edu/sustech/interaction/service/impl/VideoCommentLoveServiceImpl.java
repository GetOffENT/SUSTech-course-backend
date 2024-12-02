package edu.sustech.interaction.service.impl;

import edu.sustech.interaction.entity.VideoCommentLike;
import edu.sustech.interaction.mapper.VideoCommentLikeMapper;
import edu.sustech.interaction.service.VideoCommentLoveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频评论点赞列表 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-12-03
 */
@Service
public class VideoCommentLoveServiceImpl extends ServiceImpl<VideoCommentLikeMapper, VideoCommentLike> implements VideoCommentLoveService {

}
