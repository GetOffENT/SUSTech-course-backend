package edu.sustech.course.service;

import edu.sustech.course.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.course.entity.dto.ChapterInfoDTO;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
public interface ChapterService extends IService<Chapter> {

    /**
     * 新增课程章节
     *
     * @param chapterInfoDTO 章节信息
     * @return 章节id
     */
    Map<String, Long> addChapter(ChapterInfoDTO chapterInfoDTO);
}
