package edu.sustech.resource;

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
 * @since 2024-11-18 4:09
 */
@EnableFeignClients(basePackages = "edu.sustech.api.client", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }
}
