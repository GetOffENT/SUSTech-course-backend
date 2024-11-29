package edu.sustech.common.enums;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-30 1:19
 */
public enum Role {

    USER("用户"),
    ADMIN("管理员");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
