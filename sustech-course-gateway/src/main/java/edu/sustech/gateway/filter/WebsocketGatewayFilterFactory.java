package edu.sustech.gateway.filter;

import edu.sustech.common.constant.AuthorizationConstant;
import edu.sustech.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-15 7:28
 */
@Component
@RequiredArgsConstructor
public class WebsocketGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final JwtTool jwtTool;

    @Override
    public GatewayFilter apply(Object config) {
        return new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                String token = request.getQueryParams().getFirst(AuthorizationConstant.AUTHORIZATION);
                if (token != null) {
                    Long userId;
                    try {
                        userId = jwtTool.parseToken(token);
                    } catch (Exception ignored) {
                        userId = null;
                        ServerHttpRequest mutatedRequest = request.mutate()
                                .uri(UriComponentsBuilder.fromUri(request.getURI())
                                        .replaceQueryParam(AuthorizationConstant.AUTHORIZATION)  // 删除原来的 Authorization 参数
                                        .build().toUri())
                                .build();

                        // 使用修改后的请求继续处理
                        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                        return chain.filter(mutatedExchange);
                    }
                    if (userId != null) {
                        // 将原始请求的 Authorization 参数替换为 userId
                        ServerHttpRequest mutatedRequest = request.mutate()
                                .uri(UriComponentsBuilder.fromUri(request.getURI())
                                        .replaceQueryParam(AuthorizationConstant.AUTHORIZATION)  // 删除原来的 Authorization 参数
                                        .replaceQueryParam("userId", userId)  // 添加新的 userId 参数
                                        .build().toUri())
                                .build();

                        // 使用修改后的请求继续处理
                        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                        return chain.filter(mutatedExchange);
                    }
                }

                return chain.filter(exchange);
            }
        }, 0);
    }
}
