package edu.sustech.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.CaptchaConstant;
import edu.sustech.common.exception.RegisterException;
import edu.sustech.common.result.Result;
import edu.sustech.user.entity.User;
import edu.sustech.user.entity.dto.FoundByEmailDTO;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;
import edu.sustech.user.mapper.UserMapper;
import edu.sustech.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final CaptchaService captchaService;

    /**
     * 邮箱注册
     *
     * @param registerByEmailDTO 注册信息
     */
    @Override
    public void register(RegisterByEmailDTO registerByEmailDTO) {
        // 图形验证码二次校验
//        captchaVerification(registerByEmailDTO.getCaptchaVerification());

        Long count = baseMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, registerByEmailDTO.getEmail()));

        if (count > 0) {
            throw new RegisterException("用户已存在");
        }

        checkCaptcha(registerByEmailDTO.getCaptcha(), registerByEmailDTO.getEmail());

        User user = BeanUtil.copyProperties(registerByEmailDTO, User.class);
        // 使用hutool工具包随机字符串
        user.setNickname("SUSTecher" + RandomUtil.randomString(10));
        int insert = baseMapper.insert(user);
        if (insert == 0) {
            throw new RegisterException("服务器繁忙，请稍后再试");
        }
    }


    /**
     * 找回密码
     *
     * @param foundByEmailDTO 找回密码信息
     */
    @Override
    public void foundPassword(FoundByEmailDTO foundByEmailDTO) {
        // 图形验证码二次校验
//        captchaVerification(foundByEmailDTO.getCaptchaVerification());

        Long count = baseMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, foundByEmailDTO.getEmail()));
        if (count == 0) {
            throw new RegisterException("用户未注册");
        }

        checkCaptcha(foundByEmailDTO.getCaptcha(), foundByEmailDTO.getEmail());

        User user = BeanUtil.copyProperties(foundByEmailDTO, User.class);
        int insert = baseMapper.update(
                user,
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, foundByEmailDTO.getEmail())
        );
        if (insert == 0) {
            throw new RegisterException("服务器繁忙，请稍后再试");
        }
    }

    /**
     * 判断邮箱是否已经注册
     *
     * @param email 邮箱
     * @param type  操作类型
     * @return 用户邮箱
     */
    @Override
    public Result<String> judgeEmail(String email, String type) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (type.equals("register")) {
            if (user == null) {
                return Result.success();
            }
            return Result.error("该邮箱已注册");
        } else if (type.equals("found")) {
            if (user != null) {
                return Result.success();
            }
            return Result.error("该邮箱未注册");
        }
        return Result.error("服务器繁忙，请稍后再试");
    }

    // 检查redis中的验证码
    private void checkCaptcha(String captcha, String email) {
        String key = CaptchaConstant.REGISTER_EMAIL_Captcha + email;
        // 检查redis中的验证码
        // 如果不存在
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            throw new RegisterException("验证码未发送或已过期");
        }

        String captcha2 = (String) redisTemplate.opsForValue().get(key);
        if (!captcha.equals(captcha2)) {
            throw new RegisterException("验证码错误");
        }
    }


    /**
     * 图形验证码二次校验
     *
     * @param captchaVerification 校验码
     */
    private void captchaVerification(String captchaVerification) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            String repCode = response.getRepCode();
            log.error("图形验证码校验失败: {}", repCode);
            throw new RegisterException("验证码校验失败，请重新获取");
        }
    }
}
