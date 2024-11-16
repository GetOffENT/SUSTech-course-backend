package edu.sustech.course.service.impl;

import edu.sustech.course.entity.CourseDescription;
import edu.sustech.course.mapper.CourseDescriptionMapper;
import edu.sustech.course.service.CourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-16
 */
@Service
public class CourseDescriptionServiceImpl extends ServiceImpl<CourseDescriptionMapper, CourseDescription> implements CourseDescriptionService {

}
