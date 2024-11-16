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
 * @since 2024-11-16 6:43
 */
@Getter
public enum LikeEnum {
    NONE(0, "未操作"),
    LIKE(1, "点赞"),
    DISLIKE(2, "点踩");

    @JsonValue
    @EnumValue
    private final Integer code;
    private final String desc;

    LikeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
