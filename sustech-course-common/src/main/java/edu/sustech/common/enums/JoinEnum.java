package edu.sustech.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * <p>
 * 加入状态枚举
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-16 6:52
 */
@Getter
public enum JoinEnum {
    NONE(0, "未加入"),
    APPLYING(1, "申请中"),
    JOINED(2, "已加入"),
    REJECTED(3, "申请被拒绝"),
    QUIT(4, "已退出");

    @JsonValue
    @EnumValue
    private final Integer code;
    private final String desc;

    JoinEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
