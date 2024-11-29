package edu.sustech.gateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import edu.sustech.common.enums.Role;
import edu.sustech.gateway.properties.JwtProperties;
import edu.sustech.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-09 15:05
 */
@Component
@RequiredArgsConstructor
public class LoginGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public GatewayFilter apply(Object config) {
        return new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                ServerHttpRequest request = exchange.getRequest();
                String path = request.getPath().toString();
                if (!antPathMatcher.match(jwtProperties.getLoginPath(), path)) {
                    return chain.filter(exchange);
                }

                ResponseDecorator decorator;
                if (antPathMatcher.match("/user/**/login", path)) {
                    decorator = new ResponseDecorator(exchange.getResponse(), jwtTool, jwtProperties, Role.USER);
                } else {
                    decorator = new ResponseDecorator(exchange.getResponse(), jwtTool, jwtProperties, Role.ADMIN);
                }
                return chain.filter(exchange.mutate().response(decorator).build());
            }
        }, -1000);
    }
}
