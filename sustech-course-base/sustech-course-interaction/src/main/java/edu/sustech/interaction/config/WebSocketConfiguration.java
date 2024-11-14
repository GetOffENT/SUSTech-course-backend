package edu.sustech.interaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <p>
 * WebSocket配置类，用于注册WebSocket的Bean
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-13 16:23
 */
@Configuration
public class WebSocketConfiguration {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
