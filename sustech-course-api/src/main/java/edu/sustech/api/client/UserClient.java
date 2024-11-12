package edu.sustech.api.client;

import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-12 7:48
 */
@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/user/user/info/{uid}")
    Result<UserDTO> getUserAndCoursesById(@PathVariable Long uid);

}
