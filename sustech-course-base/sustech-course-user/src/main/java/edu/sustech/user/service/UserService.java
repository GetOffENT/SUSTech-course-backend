package edu.sustech.user.service;

import edu.sustech.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
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
     * @param registerByEmailDTO 注册信息
     * @return 用户id
     */
    void register(RegisterByEmailDTO registerByEmailDTO);
}
