package edu.sustech.interaction.controller;

import edu.sustech.common.result.Result;
import edu.sustech.interaction.entity.dto.AssignmentDTO;
import edu.sustech.interaction.entity.dto.AssignmentGradeDTO;
import edu.sustech.interaction.entity.dto.AssignmentSubmitDTO;
import edu.sustech.interaction.entity.vo.AssignmentAndAssignmentUserVO;
import edu.sustech.interaction.entity.vo.AssignmentUserVO;
import edu.sustech.interaction.entity.vo.AssignmentVO;
import edu.sustech.interaction.service.AssignmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程作业 前端控制器
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/interaction/assignment")
@Slf4j
@Api(tags = "作业相关接口")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    /**
     * 获取课程作业列表
     *
     * @param courseId 课程ID
     * @return 课程作业列表
     */
    @GetMapping("/list")
    @ApiOperation("获取课程作业列表")
    public Result<List<AssignmentVO>> list(@RequestParam Long courseId) {
        log.info("获取课程作业列表 courseId:{}", courseId);
        return Result.success(assignmentService.listAssignment(courseId));
    }

    /**
     * 获取学生作业记录列表
     *
     * @param assignmentId 作业ID
     * @return 学生作业记录列表
     */
    @PostMapping("/student/list")
    @ApiOperation("获取学生作业记录列表")
    public Result<List<AssignmentUserVO>> listAssignmentUser(
            @RequestParam Long assignmentId,
            @RequestParam List<Long> userIds
    ) {
        log.info("获取学生作业记录列表 assignmentId:{}", assignmentId);
        return Result.success(assignmentService.listAssignmentUser(assignmentId, userIds));
    }

    /**
     * 更新作业状态
     *
     * @param id     作业ID
     * @param status 作业状态
     */
    @PostMapping("/status/{id}")
    @ApiOperation("更新作业状态")
    public Result<Object> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("更新作业状态 assignmentId:{} status:{}", id, status);
        assignmentService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 新建或更新作业
     *
     * @param assignmentDTO 作业信息
     * @return 作业信息(含id等)
     */
    @PostMapping
    @ApiOperation("新建作业")
    public Result<AssignmentVO> createOrUpdateAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        log.info("新建作业: {}", assignmentDTO);
        return Result.success(assignmentService.createOrUpdateAssignment(assignmentDTO));
    }

    /**
     * 删除作业
     *
     * @param id 作业ID
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除作业")
    public Result<Object> deleteAssignment(@PathVariable Long id) {
        log.info("删除作业 assignmentId:{}", id);
        assignmentService.deleteAssignment(id);
        return Result.success();
    }


    /**
     * 批改作业
     *
     * @param assignmentGradeDTO 作业批改信息
     */
    @PostMapping("/grade")
    @ApiOperation("批改作业")
    public Result<Object> gradeAssignment(@RequestBody AssignmentGradeDTO assignmentGradeDTO) {
        log.info("批改作业 assignmentGradeDTO:{}", assignmentGradeDTO);
        assignmentService.gradeAssignment(assignmentGradeDTO);
        return Result.success();
    }

    /**
     * 提交作业
     *
     * @param assignmentSubmitDTO 作业提交信息
     */
    @PostMapping("/submit")
    @ApiOperation("提交作业")
    public Result<Object> submitAssignment(@RequestBody AssignmentSubmitDTO assignmentSubmitDTO) {
        log.info("提交作业 assignmentSubmitDTO:{}", assignmentSubmitDTO);
        assignmentService.submitAssignment(assignmentSubmitDTO);
        return Result.success();
    }

    /**
     * 学生根据课程ID获取作业以及作业记录列表
     *
     * @param courseId 课程ID
     * @return (作业以及作业记录)列表
     */
    @GetMapping("/info/list")
    @ApiOperation("学生根据课程ID获取作业及作业记录列表")
    public Result<List<AssignmentAndAssignmentUserVO>> getAssignmentAndInfoList(@RequestParam Long courseId) {
        log.info("学生根据课程ID获取作业及作业记录列表, assignmentId:{}", courseId);
        return Result.success(assignmentService.getAssignmentAndInfoList(courseId));
    }
}
