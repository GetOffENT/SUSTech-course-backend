package edu.sustech.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.TeacherException;
import edu.sustech.common.util.UserContext;
import edu.sustech.user.entity.Teacher;
import edu.sustech.user.entity.dto.TeacherDTO;
import edu.sustech.user.mapper.TeacherMapper;
import edu.sustech.user.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 教师表 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-16
 */
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    private final UserClient userClient;

    /**
     * 新增或更新教师信息
     *
     * @param teacherDTO 教师信息
     */
    @Override
    public void addOrUpdateTeacherInfo(TeacherDTO teacherDTO) {
        Long userId = checkTeacher();

        Teacher teacher = baseMapper.selectById(userId);
        if (teacher == null) {
            Teacher insertInfo = Teacher.builder()
                    .id(userId)
                    .teacherName(teacherDTO.getTeacherName())
                    .teacherInfo(teacherDTO.getTeacherInfo())
                    .teacherAvatar(teacherDTO.getTeacherAvatar())
                    .build();
            int insert = baseMapper.insert(insertInfo);
            if (insert == 0) {
                throw new TeacherException(MessageConstant.TEACHER_INFO_ADD_FAILED);
            }
        } else {
            if (!teacher.getId().equals(userId)) {
                throw new TeacherException(MessageConstant.NO_PERMISSION);
            }
            BeanUtil.copyProperties(teacherDTO, teacher);
            int update = baseMapper.updateById(teacher);
            if (update == 0) {
                throw new TeacherException(MessageConstant.TEACHER_INFO_UPDATE_FAILED);
            }
        }
    }

    public Long checkTeacher() {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new TeacherException(MessageConstant.NOT_LOGIN);
        }

        UserDTO user = userClient.getUserById(userId).getData();
        if (user.getRole() != 2) {
            throw new TeacherException(MessageConstant.NOT_TEACHER);
        }
        return userId;
    }
}
