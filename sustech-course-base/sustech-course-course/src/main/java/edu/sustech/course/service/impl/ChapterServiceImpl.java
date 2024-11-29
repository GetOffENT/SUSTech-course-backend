package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.course.entity.Chapter;
import edu.sustech.course.entity.dto.ChapterInfoDTO;
import edu.sustech.course.mapper.ChapterMapper;
import edu.sustech.course.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.course.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final CommonUtil commonUtil;

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
        // TODO
    }
}
