package edu.sustech.interaction.service.impl;

import edu.sustech.api.client.CourseClient;
import edu.sustech.api.entity.dto.VideoDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CommentException;
import edu.sustech.common.util.UserContext;
import edu.sustech.interaction.entity.Danmu;
import edu.sustech.interaction.mapper.DanmuMapper;
import edu.sustech.interaction.service.DanmuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 弹幕表 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, Danmu> implements DanmuService {

    private final CourseClient courseClient;

    /**
     * 删除弹幕
     *
     * @param id 弹幕id
     */
    @Override
    public void deleteDanmu(Long id) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CommentException(MessageConstant.NOT_LOGIN);
        }

        Danmu danmu = baseMapper.selectById(id);
        if (danmu == null) {
            throw new CommentException(MessageConstant.DANMU_NOT_EXIST);
        }

        VideoDTO videoDTO = courseClient.getVideoById(danmu.getVideoId()).getData();
        if (!danmu.getUserId().equals(userId) && !Objects.equals(videoDTO.getUserId(), userId)) {
            throw new CommentException(MessageConstant.DANMU_NO_PERMISSION);
        }

        int count = baseMapper.deleteById(id);
        if (count == 0) {
            throw new CommentException(MessageConstant.DANMU_DELETE_ERROR);
        }

        // 更新弹幕数量
        courseClient.updateDanmuCount(danmu.getVideoId(), -count);

        log.info("删除弹幕{}", id);
    }
}
