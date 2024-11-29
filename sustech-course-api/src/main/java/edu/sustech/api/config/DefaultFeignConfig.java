package edu.sustech.api.config;

import edu.sustech.common.constant.AuthorizationConstant;
import edu.sustech.common.util.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-08 4:23
 */
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor userIdRequestInterceptor() {
        return template -> {
            // 获取登录用户
            Long userId = UserContext.getUser();
            if (userId == null) {
                // 如果为空则直接跳过
                return;
            }
            // 如果不为空则放入请求头中，传递给下游微服务
            template.header(AuthorizationConstant.USER_ID, userId.toString()).header(AuthorizationConstant.ROLE, UserContext.getRole().toString());
        };
    }
}
