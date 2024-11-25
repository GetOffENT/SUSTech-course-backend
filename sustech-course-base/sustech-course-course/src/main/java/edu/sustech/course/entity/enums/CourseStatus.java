package edu.sustech.course.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-18 15:31
 */
@Getter
public enum CourseStatus {

    /**
     * 审核中
     */
    PENDING(0, "审核中"),
    /**
     * 已过审
     */
    PASSED(1, "已过审"),
    /**
     * 未过审
     */
    NOT_PASSED(2, "未过审"),
    /**
     * 已删除
     */
    DELETED(3, "已删除"),
//    /**
//     * 已发布(目前过审就相当于自动发布了)
//     */
//    PUBLISHED(4, "已发布"),
    /**
     * 已私密
     */
    PRIVATE(5, "已私密"),
    /**
     * 编辑中
     */
    EDITING(6, "编辑中");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    CourseStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
