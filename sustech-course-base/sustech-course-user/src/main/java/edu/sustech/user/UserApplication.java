package edu.sustech.user;

import edu.sustech.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Yuxian Wu
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-11-06 5:05
 */
@EnableFeignClients(basePackages = "edu.sustech.api.client", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
@ComponentScan(basePackages = "edu.sustech")
@MapperScan("edu.sustech.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
