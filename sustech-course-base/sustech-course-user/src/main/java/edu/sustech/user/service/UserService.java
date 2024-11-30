package edu.sustech.user.service;

import edu.sustech.api.entity.dto.StudentDTO;
import edu.sustech.common.result.Result;
import edu.sustech.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.user.entity.dto.FoundByEmailDTO;
import edu.sustech.user.entity.dto.LoginByEmailDTO;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;
import edu.sustech.api.entity.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-09
 */
public interface UserService extends IService<User> {

    /**
     * 邮箱注册
     *
     * @param registerByEmailDTO 注册信息
     */
    void register(RegisterByEmailDTO registerByEmailDTO);

    /**
     * 邮箱登录
     *
     * @param loginByEmailDTO 登录信息
     * @return 用户信息
     */
    Map<String, Object> login(LoginByEmailDTO loginByEmailDTO);

    /**
     * 找回密码
     *
     * @param foundByEmailDTO 找回密码信息
     */
    void foundPassword(FoundByEmailDTO foundByEmailDTO);

    /**
     * 判断邮箱是否已经注册
     *
     * @param email 邮箱
     * @param type  操作类型
     * @return 用户邮箱
     */
    Result<String> judgeEmail(String email, String type);

    /**
     * 根据用户ID获取用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    UserDTO getUserAndCoursesById(Long uid);

    /**
     * 根据用户ID获取用户信息(不包含课程信息)
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    UserDTO getUserById(Long uid);

    /**
     * 获取学生信息
     *
     * @param studentIds 学生id列表
     * @return 学生信息列表
     */
    List<StudentDTO> getStudentList(List<Long> studentIds);

    /**
     * 搜索学生信息
     *
     * @param keyword 搜索内容
     * @return 学生信息列表
     */
    List<StudentDTO> getSearchStudentList(String keyword);
}
