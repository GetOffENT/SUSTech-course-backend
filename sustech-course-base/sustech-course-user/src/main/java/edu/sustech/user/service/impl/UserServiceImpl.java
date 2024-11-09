package edu.sustech.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.CaptchaConstant;
import edu.sustech.common.exception.RegisterException;
import edu.sustech.user.entity.User;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;
import edu.sustech.user.mapper.UserMapper;
import edu.sustech.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 邮箱注册
     *
     * @param registerByEmailDTO 注册信息
     * @return 用户id
     */
    @Override
    public void register(RegisterByEmailDTO registerByEmailDTO) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, registerByEmailDTO.getEmail()));

        if (count > 0) {
            throw new RegisterException("用户已存在");
        }

        // 检查redis中的验证码
        // 如果不存在
        if (Boolean.FALSE.equals(redisTemplate.hasKey(CaptchaConstant.REGISTER_EMAIL_Captcha + registerByEmailDTO.getEmail()))) {
            throw new RegisterException("验证码未发送或已过期");
        }

        String captcha = (String) redisTemplate.opsForValue().get(CaptchaConstant.REGISTER_EMAIL_Captcha + registerByEmailDTO.getEmail());
        if (!registerByEmailDTO.getCaptcha().equals(captcha)) {
            throw new RegisterException("验证码错误");
        }

        User user = BeanUtil.copyProperties(registerByEmailDTO, User.class);
        // 使用hutool工具包随机字符串
        user.setNickname("SUSTecher" + RandomUtil.randomString(10));
        int insert = baseMapper.insert(user);
        if (insert == 0) {
            throw new RegisterException("服务器繁忙，请稍后再试");
        }
    }
}
