package edu.sustech.api.client;

import edu.sustech.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-08 3:33
 */
@FeignClient("message-service")
public interface MessageClient {

    @GetMapping("email")
    Result testToken();

}
