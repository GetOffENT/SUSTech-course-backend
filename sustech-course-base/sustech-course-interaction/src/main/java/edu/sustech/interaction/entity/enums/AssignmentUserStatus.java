package edu.sustech.interaction.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * <p>
 * 学生作业完成状态
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-02 10:10
 */
@Getter
public enum AssignmentUserStatus {
    /**
     * 未提交
     */
    NOT_SUBMITTED(0, "未提交"),
    /**
     * 已提交
     */
    SUBMITTED(1, "已提交"),
    /**
     * 已批改
     */
    GRADED(2, "已批改"),
    /**
     * 逾期
     */
    OVERDUE(3, "逾期");

    @JsonValue
    @EnumValue
    private final int value;
    private final String description;

    AssignmentUserStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

}
