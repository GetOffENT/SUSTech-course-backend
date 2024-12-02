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
 * @since 2024-12-03 5:20
 */
@Getter
public enum CourseReviewLikeStatus {
    NONE(0, "未操作"),
    LIKE(1, "点赞"),
    DISLIKE(2, "点踩");

    @JsonValue
    @EnumValue
    private final Integer code;
    private final String desc;

    CourseReviewLikeStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
