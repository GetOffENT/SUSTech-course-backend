package edu.sustech.user.service;

import edu.sustech.user.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.user.entity.dto.TeacherDTO;

/**
 * <p>
 * 教师表 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-16
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 新增或更新教师信息
     *
     * @param teacherDTO 教师信息
     */
    void addOrUpdateTeacherInfo(TeacherDTO teacherDTO);
}
