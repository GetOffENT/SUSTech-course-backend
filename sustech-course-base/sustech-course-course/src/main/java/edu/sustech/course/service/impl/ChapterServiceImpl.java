package edu.sustech.course.service.impl;

import edu.sustech.course.entity.Chapter;
import edu.sustech.course.mapper.ChapterMapper;
import edu.sustech.course.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

}
