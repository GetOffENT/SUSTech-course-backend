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
 * @since 2024-11-16 6:52
 */
@Getter
public enum JoinEnum {
    NONE(0, "未加入"),
    JOIN(1, "加入"),
    DISJOIN(2, "退出");

    @JsonValue
    @EnumValue
    private final Integer code;
    private final String desc;

    JoinEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
