package edu.sustech.message;

import edu.sustech.api.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-06 23:15
 */
@EnableFeignClients(basePackages = "edu.sustech.api.client", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
