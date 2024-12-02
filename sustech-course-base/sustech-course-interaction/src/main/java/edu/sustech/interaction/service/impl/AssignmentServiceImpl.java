package edu.sustech.interaction.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import edu.sustech.api.client.CourseClient;
import edu.sustech.api.entity.dto.CourseInfoDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.enums.ResultCode;
import edu.sustech.common.exception.AssignmentException;
import edu.sustech.common.exception.CourseException;
import edu.sustech.common.result.Result;
import edu.sustech.common.util.UserContext;
import edu.sustech.interaction.entity.Assignment;
import edu.sustech.interaction.entity.AssignmentUser;
import edu.sustech.interaction.entity.dto.AssignmentDTO;
import edu.sustech.interaction.entity.dto.AssignmentGradeDTO;
import edu.sustech.interaction.entity.dto.AssignmentSubmitDTO;
import edu.sustech.interaction.entity.enums.AssignmentStatus;
import edu.sustech.interaction.entity.enums.AssignmentUserStatus;
import edu.sustech.interaction.entity.vo.AssignmentAndAssignmentUserVO;
import edu.sustech.interaction.entity.vo.AssignmentUserVO;
import edu.sustech.interaction.entity.vo.AssignmentVO;
import edu.sustech.interaction.mapper.AssignmentMapper;
import edu.sustech.interaction.mapper.AssignmentUserMapper;
import edu.sustech.interaction.service.AssignmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * &#x8BFE;&#x7A0B;&#x4F5C;&#x4E1A; &#x670D;&#x52A1;&#x5B9E;&#x73B0;&#x7C7B;
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-12-02
 */
@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl extends ServiceImpl<AssignmentMapper, Assignment> implements AssignmentService {

    private final AssignmentUserMapper assignmentUserMapper;

    private final CourseClient courseClient;

    /**
     * 获取课程作业列表
     *
     * @param courseId 课程ID
     * @return 课程作业列表
     */
    @Override
    public List<AssignmentVO> listAssignment(Long courseId) {
        Long userId = checkUser();
        List<Assignment> assignments = baseMapper.selectList(
                new LambdaQueryWrapper<Assignment>()
                        .eq(Assignment::getCourseId, courseId)
        );
        if (CollUtil.isEmpty(assignments)) {
            return List.of();
        }

        // 检查是否是作业的发布者
        if (!userId.equals(assignments.get(0).getUserId())) {
            throw new AssignmentException(MessageConstant.NO_PERMISSION);
        }

        return BeanUtil.copyToList(assignments, AssignmentVO.class);
    }

    /**
     * 获取学生作业记录列表
     *
     * @param assignmentId 作业ID
     * @return 学生作业记录列表
     */
    @Override
    public List<AssignmentUserVO> listAssignmentUser(Long assignmentId, List<Long> userIds) {

        Assignment assignment = checkUserAndAssignment(assignmentId);

        // 课程没有学生则返回空列表
        if (CollUtil.isEmpty(userIds)) {
            return List.of();
        }
        List<AssignmentUser> assignmentUserList = assignmentUserMapper.selectList(
                new LambdaQueryWrapper<AssignmentUser>()
                        .eq(AssignmentUser::getAssignmentId, assignmentId)
        );
        // 如果作业记录未创建，补全作业记录
        if (assignmentUserList.size() < userIds.size()) {
            Set<Long> existUserIds = assignmentUserList.stream().map(AssignmentUser::getUserId).collect(Collectors.toSet());
            List<AssignmentUser> newAssignmentUserList = new ArrayList<>();
            userIds.stream()
                    .filter(userId -> !existUserIds.contains(userId))
                    .forEach(userId -> {
                        AssignmentUser assignmentUser = AssignmentUser.builder()
                                .assignmentId(assignmentId)
                                .userId(userId)
                                .gmtCreate(LocalDateTime.now())
                                .gmtModified(LocalDateTime.now())
                                .build();
                        assignmentUser.setId(IdWorker.getId(assignmentUser));
                        if (assignment.getDeadline() != null && LocalDateTime.now().isAfter(assignment.getDeadline())) {
                            assignmentUser.setStatus(AssignmentUserStatus.OVERDUE);
                        } else {
                            assignmentUser.setStatus(AssignmentUserStatus.NOT_SUBMITTED);
                        }
                        newAssignmentUserList.add(assignmentUser);
                    });
            if (CollUtil.isNotEmpty(newAssignmentUserList)) {
                int insert = assignmentUserMapper.insertBatch(newAssignmentUserList);
                if (insert != newAssignmentUserList.size()) {
                    throw new AssignmentException(MessageConstant.ASSIGNMENT_QUERY_FAILED);
                }
            }
            assignmentUserList.addAll(newAssignmentUserList);
        }

        return BeanUtil.copyToList(assignmentUserList, AssignmentUserVO.class);
    }

    /**
     * 更新作业状态
     *
     * @param id     作业ID
     * @param status 作业状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        checkUserAndAssignment(id);
        AssignmentStatus assignmentStatus = AssignmentStatus.of(status);
        baseMapper.updateById(
                Assignment.builder()
                        .id(id)
                        .status(assignmentStatus)
                        .build()
        );
    }

    /**
     * 新建作业
     *
     * @param assignmentDTO 作业信息
     * @return 作业信息(含id等)
     */
    @Override
    public AssignmentVO createAssignment(AssignmentDTO assignmentDTO) {
        Long userId = checkUserAndCourse(assignmentDTO.getCourseId());
        Assignment assignment =
                BeanUtil.copyProperties(assignmentDTO, Assignment.class)
                        .setUserId(userId)
                        .setStatus(AssignmentStatus.NOT_PUBLISHED);
        baseMapper.insert(assignment);
        return BeanUtil.copyProperties(assignment, AssignmentVO.class);
    }

    /**
     * 删除作业
     *
     * @param id 作业ID
     */
    @Override
    public void deleteAssignment(Long id) {
        checkUserAndAssignment(id);
        int row = baseMapper.deleteById(id);
        if (row == 0) {
            throw new AssignmentException(MessageConstant.ASSIGNMENT_DELETE_FAILED);
        }
    }

    /**
     * 批改作业
     *
     * @param assignmentGradeDTO 作业批改信息
     */
    @Override
    public void gradeAssignment(AssignmentGradeDTO assignmentGradeDTO) {
        checkUserAndAssignment(assignmentGradeDTO.getAssignmentId());

        AssignmentUser assignmentUser = assignmentUserMapper.selectOne(
                new LambdaQueryWrapper<AssignmentUser>()
                        .eq(AssignmentUser::getAssignmentId, assignmentGradeDTO.getAssignmentId())
                        .eq(AssignmentUser::getUserId, assignmentGradeDTO.getUserId())
        );
        if (assignmentUser == null) {
            throw new AssignmentException(MessageConstant.ASSIGNMENT_USER_NOT_FOUND);
        }
        BeanUtil.copyProperties(assignmentGradeDTO, assignmentUser);
        assignmentUser.setGmtGrade(LocalDateTime.now()).setStatus(AssignmentUserStatus.GRADED);

        int row = assignmentUserMapper.updateById(assignmentUser);
        if (row == 0) {
            throw new AssignmentException(MessageConstant.ASSIGNMENT_GRADE_FAILED);
        }
    }

    /**
     * 提交作业
     *
     * @param assignmentSubmitDTO 作业提交信息
     */
    @Override
    public void submitAssignment(AssignmentSubmitDTO assignmentSubmitDTO) {
        Long userId = checkUser();

        Assignment assignment = baseMapper.selectById(assignmentSubmitDTO.getAssignmentId());
        if (LocalDateTime.now().isAfter(assignment.getDeadline())){
            throw new AssignmentException(MessageConstant.ASSIGNMENT_EXPIRED);
        }

        AssignmentUser assignmentUser = assignmentUserMapper.selectOne(
                new LambdaQueryWrapper<AssignmentUser>()
                        .eq(AssignmentUser::getAssignmentId, assignmentSubmitDTO.getAssignmentId())
                        .eq(AssignmentUser::getUserId, userId)
        );
        if (assignmentUser == null) {
            throw new AssignmentException(MessageConstant.ASSIGNMENT_USER_NOT_FOUND);
        }
        BeanUtil.copyProperties(assignmentSubmitDTO, assignmentUser);
        assignmentUser.setGmtSubmit(LocalDateTime.now()).setStatus(AssignmentUserStatus.SUBMITTED);

        int row = assignmentUserMapper.updateById(assignmentUser);
        if (row == 0) {
            throw new AssignmentException(MessageConstant.ASSIGNMENT_SUBMIT_FAILED);
        }
    }

    /**
     * 学生根据课程ID获取作业以及作业记录列表
     *
     * @param courseId 课程ID
     * @return (作业以及作业记录)列表
     */
    @Override
    @Transactional
    public List<AssignmentAndAssignmentUserVO> getAssignmentAndInfoList(Long courseId) {
        Long userId = checkUser();

        List<Assignment> assignments = baseMapper.selectList(
                new LambdaQueryWrapper<Assignment>()
                        .eq(Assignment::getCourseId, courseId)
        );
        if (CollUtil.isEmpty(assignments)) {
            return List.of();
        }

        Set<Long> assignmentIds = assignments.stream().map(Assignment::getId).collect(Collectors.toSet());
        List<AssignmentUser> assignmentUserList = assignmentUserMapper.selectList(
                new LambdaQueryWrapper<AssignmentUser>()
                        .in(AssignmentUser::getAssignmentId, assignmentIds)
        );

        // 作业ID到作业的映射
        Map<Long, Assignment> assignmentMap = assignments.stream().collect(Collectors.toMap(Assignment::getId, assignment -> assignment));

        // 检查作业是否逾期未交，更新状态
        assignmentUserList.forEach(assignmentUser -> {
            if (assignmentUser.getGmtSubmit() == null && assignmentUser.getGmtGrade() == null) {
                Assignment assignment = assignmentMap.get(assignmentUser.getAssignmentId());
                if (assignment != null && assignment.getDeadline() != null && LocalDateTime.now().isAfter(assignment.getDeadline())) {
                    assignmentUser.setStatus(AssignmentUserStatus.OVERDUE);
                    assignmentUserMapper.updateById(assignmentUser);
                }
            }
        });

        // 如果作业记录未创建，补全作业记录(用于学生第一次查看作业或者老师新发布作业)
        if (assignmentUserList.size() < assignmentIds.size()) {
            Set<Long> existAssignmentIds = assignmentUserList.stream().map(AssignmentUser::getAssignmentId).collect(Collectors.toSet());

            List<AssignmentUser> newAssignmentUserList = new ArrayList<>();
            assignments.stream()
                    .filter(assignment -> !existAssignmentIds.contains(assignment.getId()))
                    .forEach(assignment -> {
                        AssignmentUser assignmentUser = AssignmentUser.builder()
                                .assignmentId(assignment.getId())
                                .userId(userId)
                                .gmtCreate(LocalDateTime.now())
                                .gmtModified(LocalDateTime.now())
                                .build();
                        assignmentUser.setId(IdWorker.getId(assignment));
                        if (assignment.getDeadline() != null && LocalDateTime.now().isAfter(assignment.getDeadline())) {
                            assignmentUser.setStatus(AssignmentUserStatus.OVERDUE);
                        } else {
                            assignmentUser.setStatus(AssignmentUserStatus.NOT_SUBMITTED);
                        }
                        newAssignmentUserList.add(assignmentUser);
                    });
            if (CollUtil.isNotEmpty(newAssignmentUserList)) {
                int insert = assignmentUserMapper.insertBatch(newAssignmentUserList);
                if (insert != newAssignmentUserList.size()) {
                    throw new AssignmentException(MessageConstant.ASSIGNMENT_QUERY_FAILED);
                }
            } else {
                throw new AssignmentException(MessageConstant.ASSIGNMENT_QUERY_FAILED);
            }

            assignmentUserList.addAll(newAssignmentUserList);
        }

        // 作业ID到作业记录的映射
        Map<Long, AssignmentUser> map =
                assignmentUserList.stream().collect(Collectors.toMap(AssignmentUser::getAssignmentId, assignmentUser -> assignmentUser));

        List<AssignmentAndAssignmentUserVO> assignmentAndAssignmentUserVOs =
                BeanUtil.copyToList(assignments, AssignmentAndAssignmentUserVO.class, CopyOptions.create().setIgnoreProperties("status"));
        assignmentAndAssignmentUserVOs.forEach(
                assignmentAndAssignmentUserVO ->
                        BeanUtil.copyProperties(map.get(assignmentAndAssignmentUserVO.getId()), assignmentAndAssignmentUserVO, "id")
        );
        return assignmentAndAssignmentUserVOs;
    }

    public Long checkUser() {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CourseException(MessageConstant.NOT_LOGIN);
        }
        return userId;
    }

    public Long checkUserAndCourse(Long courseId) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CourseException(MessageConstant.NOT_LOGIN);
        }

        Result<CourseInfoDTO> courseById = courseClient.getCourseById(courseId);
        if (Objects.equals(courseById.getCode(), ResultCode.SUCCESS.code())){
            CourseInfoDTO course = courseById.getData();
            if (course == null) {
                throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
            }
            if (!userId.equals(course.getUserId())) {
                throw new CourseException(MessageConstant.NO_PERMISSION);
            }
        } else {
            throw new CourseException(MessageConstant.COURSE_NOT_EXIST);
        }
        return userId;
    }

    public Assignment checkUserAndAssignment(Long assignmentId) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CourseException(MessageConstant.NOT_LOGIN);
        }

        Assignment assignment = baseMapper.selectById(assignmentId);
        if (assignment == null) {
            throw new AssignmentException(MessageConstant.ASSIGNMENT_NOT_EXIST);
        }
        if (!userId.equals(assignment.getUserId())) {
            throw new AssignmentException(MessageConstant.NO_PERMISSION);
        }
        return assignment;
    }
}
