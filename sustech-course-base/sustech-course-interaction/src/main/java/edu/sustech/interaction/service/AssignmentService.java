package edu.sustech.interaction.service;

import edu.sustech.interaction.entity.Assignment;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.interaction.entity.dto.AssignmentDTO;
import edu.sustech.interaction.entity.dto.AssignmentGradeDTO;
import edu.sustech.interaction.entity.dto.AssignmentSubmitDTO;
import edu.sustech.interaction.entity.vo.AssignmentAndAssignmentUserVO;
import edu.sustech.interaction.entity.vo.AssignmentUserVO;
import edu.sustech.interaction.entity.vo.AssignmentVO;

import java.util.List;

/**
 * <p>
 * 课程作业 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-12-02
 */
public interface AssignmentService extends IService<Assignment> {

    /**
     * 获取课程作业列表
     *
     * @param courseId 课程ID
     * @return 课程作业列表
     */
    List<AssignmentVO> listAssignment(Long courseId);

    /**
     * 获取学生作业记录列表
     *
     * @param assignmentId 作业ID
     * @return 学生作业记录列表
     */
    List<AssignmentUserVO> listAssignmentUser(Long assignmentId, List<Long> userIds);

    /**
     * 更新作业状态
     *
     * @param id     作业ID
     * @param status 作业状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 新建作业
     *
     * @param assignmentDTO 作业信息
     * @return 作业信息(含id等)
     */
    AssignmentVO createAssignment(AssignmentDTO assignmentDTO);

    /**
     * 删除作业
     *
     * @param id 作业ID
     */
    void deleteAssignment(Long id);

    /**
     * 批改作业
     *
     * @param assignmentGradeDTO 作业批改信息
     */
    void gradeAssignment(AssignmentGradeDTO assignmentGradeDTO);

    /**
     * 提交作业
     *
     * @param assignmentSubmitDTO 作业提交信息
     */
    void submitAssignment(AssignmentSubmitDTO assignmentSubmitDTO);

    /**
     * 学生根据课程ID获取作业以及作业记录列表
     *
     * @param courseId 课程ID
     * @return (作业以及作业记录)列表
     */
    List<AssignmentAndAssignmentUserVO> getAssignmentAndInfoList(Long courseId);
}
