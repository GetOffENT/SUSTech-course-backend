package edu.sustech.user.service.impl;

import edu.sustech.user.entity.User;
import edu.sustech.user.mapper.UserMapper;
import edu.sustech.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
