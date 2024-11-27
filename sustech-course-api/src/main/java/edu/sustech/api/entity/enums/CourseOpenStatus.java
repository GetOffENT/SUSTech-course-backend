package edu.sustech.api.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-18 15:25
 */
@Getter
public enum CourseOpenStatus {
    /**
     * 不公开
     */
    NOT_OPEN(0, "不公开"),
    /**
     * 部分公开
     */
    PART_OPEN(1, "部分公开"),
    /**
     * 完全公开
     */
    FULL_OPEN(2, "完全公开");

    @JsonValue
    @EnumValue
    private final int value;
    private final String description;

    CourseOpenStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
