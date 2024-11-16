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
 * @since 2024-11-16 17:57
 */
@Getter
public enum StateEnum {
    NORMAL(0, "正常"),
    BAN(1, "封禁"),
    CANCEL(2, "注销");

    @JsonValue
    @EnumValue
    private final int code;
    private final String desc;

    StateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
