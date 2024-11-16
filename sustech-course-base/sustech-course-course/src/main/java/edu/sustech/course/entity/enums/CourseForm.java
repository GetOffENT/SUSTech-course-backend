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
 * @since 2024-11-16 13:43
 */
@Getter
public enum CourseForm {
    COURSEWARE(1, "课件"),
    VIDEO(2, "视频"),
    LIVE(3, "直播");

    @JsonValue
    @EnumValue
    private final Integer code;
    private final String name;

    CourseForm(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
