package edu.sustech.common.interceptor;

import cn.hutool.core.util.StrUtil;
import edu.sustech.common.constant.AuthorizationConstant;
import edu.sustech.common.enums.Role;
import edu.sustech.common.util.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-08 2:53
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader(AuthorizationConstant.USER_ID);

        // 如果userId不为空，设置到UserContext中
        if (StrUtil.isNotBlank(userId)) {
            UserContext.setUser(Map.of(
                    "userId", Long.valueOf(userId),
                    "role", Role.valueOf(request.getHeader(AuthorizationConstant.ROLE))
            ));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理用户
        UserContext.removeUser();
    }
}
