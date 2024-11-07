package edu.sustech.gateway;

import edu.sustech.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-08 2:46
 */
@SpringBootTest
public class TokenTest {

    @Autowired
    private JwtTool jwtTool;

    @Test
    public void test(){
        String token = jwtTool.createToken(1L, Duration.ofDays(365));
        System.out.println(token);
    }
}
