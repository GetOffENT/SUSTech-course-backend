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
 * @since 2024-11-16 17:59
 */
@Getter
public enum Gender {
    MAN(1, "男"),
    WOMAN(0, "女"),
    UNKNOWN(2, "未知");

    @JsonValue
    @EnumValue
    private final int code;
    private final String desc;

    Gender(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
