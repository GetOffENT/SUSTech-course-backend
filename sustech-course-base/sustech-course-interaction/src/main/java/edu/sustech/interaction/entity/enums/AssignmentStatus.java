package edu.sustech.interaction.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-02 9:36
 */
@Getter
public enum AssignmentStatus {

    /**
     * 未发布
     */
    NOT_PUBLISHED(1, "未发布"),
    /**
     * 已发布
     */
    PUBLISHED(2, "已发布"),
    /**
     * 已批改
     */
    GRADED(3, "已批改");


    @JsonValue
    @EnumValue
    private final Integer value;
    private final String description;

    AssignmentStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AssignmentStatus of(Integer value) {
        for (AssignmentStatus status : AssignmentStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
