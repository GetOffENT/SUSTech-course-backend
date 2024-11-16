package edu.sustech.user.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-16 17:55
 */
@Getter
public enum RoleEnum {
    NORMAL(0, "普通"),
    STUDENT(1, "学生"),
    TEACHER(2, "教师");

    @JsonValue
    @EnumValue
    private final int code;
    private final String desc;

    RoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
