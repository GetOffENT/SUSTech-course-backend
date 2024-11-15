package edu.sustech.interaction;

import edu.sustech.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * 互动服务启动类
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-06 5:06
 */
@EnableFeignClients(basePackages = "edu.sustech.api.client", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
@MapperScan("edu.sustech.interaction.mapper")
@ComponentScan("edu.sustech")
public class InteractionApplication {
    public static void main(String[] args) {
        SpringApplication.run(InteractionApplication.class, args);
    }
}
