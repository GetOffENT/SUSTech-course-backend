package edu.sustech.user.service;

import edu.sustech.common.result.Result;
import edu.sustech.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.user.entity.dto.FoundByEmailDTO;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;

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

}
