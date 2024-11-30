package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.course.entity.Chapter;
import edu.sustech.course.entity.Course;
import edu.sustech.course.entity.Video;
import edu.sustech.course.entity.dto.ChapterInfoDTO;
import edu.sustech.course.mapper.ChapterMapper;
import edu.sustech.course.mapper.CourseMapper;
import edu.sustech.course.mapper.VideoMapper;
import edu.sustech.course.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.course.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
@Service
@RequiredArgsConstructor
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    private final CourseMapper courseMapper;

    private final VideoMapper videoMapper;

    private final CommonUtil commonUtil;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 新增课程章节
     *
     * @param chapterInfoDTO 章节信息
     * @return 章节id
     */
    @Override
    public Map<String, Long> addChapter(ChapterInfoDTO chapterInfoDTO) {
        commonUtil.checkTeacher();

        Chapter chapter = BeanUtil.copyProperties(chapterInfoDTO, Chapter.class);
        int insert = baseMapper.insert(chapter);
        if (insert == 0) {
            throw new CourseException(MessageConstant.CHAPTER_ADD_FAILED);
        }
        return Map.of("chapterId", chapter.getId());
    }

    /**
     * 删除课程章节
     *
     * @param chapterId 章节id
     */
    @Override
    public void deleteChapter(Long chapterId) {
        Long userId = commonUtil.checkTeacher();

        Chapter chapter = baseMapper.selectById(chapterId);
        if (chapter == null) {
            throw new CourseException(MessageConstant.CHAPTER_NOT_FOUND);
        }

        Course course = courseMapper.selectById(chapter.getCourseId());
        if (!course.getUserId().equals(userId)) {
            throw new CourseException(MessageConstant.NO_PERMISSION);
        }

        // 获取章节下的所有视频源ID列表
        List<String> videoSourceIdList =
                videoMapper.selectVideoSourceIdListByChapterId(chapterId)
                        .stream().filter(StrUtil::isNotBlank).toList();

        Long count = videoMapper.selectCount(new LambdaQueryWrapper<Video>().eq(Video::getChapterId, chapterId));
        if (count > 0){
            // 删除视频
            int delete = videoMapper.delete(new LambdaQueryWrapper<Video>().eq(Video::getChapterId, chapterId));
            if (delete == 0) {
                throw new CourseException(MessageConstant.CHAPTER_DELETE_FAILED);
            }
            if (CollUtil.isNotEmpty(videoSourceIdList)) {
                // 消息队列通知云端删除视频
                rabbitTemplate.convertAndSend("resource.direct", "resource.video.remove", CollUtil.join(videoSourceIdList, ","));
            }
        }

        // 删除章节
        int deleteChapter = baseMapper.deleteById(chapterId);
        if (deleteChapter == 0) {
            throw new CourseException(MessageConstant.CHAPTER_DELETE_FAILED);
        }
    }
}
