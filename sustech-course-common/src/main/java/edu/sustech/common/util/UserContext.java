package edu.sustech.common.util;

import edu.sustech.common.enums.Role;

import java.util.Map;

public class UserContext {
    private static final ThreadLocal<Map<String, Object>> tl = new ThreadLocal<>();

    /**
     * 保存当前登录用户信息到ThreadLocal
     * @param user 用户id和角色
     */
    public static void setUser(Map<String, Object> user) {
        tl.set(user);
    }

    /**
     * 获取当前登录用户信息
     * @return 用户id
     */
    public static Long getUser() {
        if (tl.get() == null) {
            return null;
        }
        return (Long) tl.get().get("userId");
    }

    public static Role getRole() {
        if (tl.get() == null) {
            return null;
        }
        return (Role) tl.get().get("role");
    }

    /**
     * 移除当前登录用户信息
     */
    public static void removeUser(){
        tl.remove();
    }
}
