package edu.sustech.api.client;

import edu.sustech.api.entity.dto.StudentDTO;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    /**
     * 根据用户ID获取用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    @GetMapping("/user/user/{uid}")
    Result<UserDTO> getUserById(@PathVariable Long uid);

    /**
     * 根据用户ID获取用户信息(包含课程数据)
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    @GetMapping("/user/user/info/{uid}")
    Result<UserDTO> getUserAndCoursesById(@PathVariable Long uid);

    /**
     * 获取学生信息
     *
     * @param studentIds 学生ID列表
     * @return 学生信息
     */
    @GetMapping("/user/user/student")
    Result<List<StudentDTO>> getStudentList(@RequestParam List<Long> studentIds);

}
