package edu.sustech.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.api.client.CourseClient;
import edu.sustech.api.entity.dto.UserCourseInfoDTO;
import edu.sustech.common.constant.CaptchaConstant;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.RegisterException;
import edu.sustech.common.result.Result;
import edu.sustech.common.result.ResultCode;
import edu.sustech.user.entity.User;
import edu.sustech.user.entity.dto.FoundByEmailDTO;
import edu.sustech.user.entity.dto.LoginByEmailDTO;
import edu.sustech.user.entity.dto.RegisterByEmailDTO;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.user.mapper.UserMapper;
import edu.sustech.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

    private final CourseClient courseClient;

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
            throw new RegisterException(MessageConstant.ACCOUNT_ALREADY_EXIST);
        }

        checkCaptcha(registerByEmailDTO.getCaptcha(), registerByEmailDTO.getEmail());

        User user = BeanUtil.copyProperties(registerByEmailDTO, User.class);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        // 使用hutool工具包随机字符串
        user.setNickname(MessageConstant.RANDOM_NICKNAME_PREFIX + RandomUtil.randomString(10));
        int insert = baseMapper.insert(user);
        if (insert == 0) {
            throw new RegisterException(MessageConstant.ERROR);
        }
    }

    /**
     * 邮箱登录
     *
     * @param loginByEmailDTO 登录信息
     * @return 用户信息
     */
    @Override
    public Map<String, Object> login(LoginByEmailDTO loginByEmailDTO) {

        // TODO: redis缓存用户信息


        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, loginByEmailDTO.getEmail()));
        if (user == null) {
            throw new RegisterException(MessageConstant.LOGIN_ERROR);
        }

        String password = DigestUtils.md5DigestAsHex(loginByEmailDTO.getPassword().getBytes());
        if (!password.equals(user.getPassword())) {
            throw new RegisterException(MessageConstant.LOGIN_ERROR);
        }

        if (user.getState() != 0) {
            throw new RegisterException(MessageConstant.ACCOUNT_LOCKED);
        }

        return Map.of("user", BeanUtil.copyProperties(user, UserDTO.class));
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
            throw new RegisterException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        checkCaptcha(foundByEmailDTO.getCaptcha(), foundByEmailDTO.getEmail());

        User user = BeanUtil.copyProperties(foundByEmailDTO, User.class);
        int insert = baseMapper.update(
                user,
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, foundByEmailDTO.getEmail())
        );
        if (insert == 0) {
            throw new RegisterException(MessageConstant.ERROR);
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
            return Result.error(MessageConstant.ACCOUNT_ALREADY_EXIST);
        } else if (type.equals("found")) {
            if (user != null) {
                return Result.success();
            }
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return Result.error(MessageConstant.ERROR);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    @Override
    public UserDTO getUserAndCoursesById(Long uid) {

        UserDTO userDTO = this.getUserById(uid);


        // TODO: 实现从redis中查询该用户发布的课程id集合(redis中以set形式存储)
//        String courseKey = "USER:COURSE:" + uid;
//        Set<Object> courseIds = redisTemplate.opsForSet().members(courseKey);
//        if (CollUtil.isEmpty(courseIds)) {
//            userVO.setVideoCount(0).setLoveCount(0).setPlayCount(0);
//            return userVO;
//        }
        // 远程调用，从mysql中查询该用户发布的课程信息
        Result<UserCourseInfoDTO> coursesInfoByUserId = courseClient.getCoursesInfoByUserId(uid);
        if (Objects.equals(coursesInfoByUserId.getCode(), ResultCode.SUCCESS.code())) {
            UserCourseInfoDTO data = coursesInfoByUserId.getData();
            userDTO.setCourseCount(data.getCourseCount())
                    .setLike(data.getLike())
                    .setPlay(data.getPlay());
            return userDTO;
        } else {
            throw new RuntimeException(coursesInfoByUserId.getMessage());
        }
    }

    /**
     * 根据用户ID获取用户信息(不包含课程信息)
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    @Override
    public UserDTO getUserById(Long uid) {
        if (uid == null) {
            return null;
        }

        // 先从redis中查询
        String key = "USER:INFO:" + uid;
        // 使用json序列化
        User user = JSONObject.parseObject((String) redisTemplate.opsForValue().get(key), User.class);

        if (user == null) {
            user = baseMapper.selectById(uid);
            if (user == null) {
                return null;
            }
            User finalUser = user;
            CompletableFuture.runAsync(() -> {
                // 存入redis
                redisTemplate.opsForValue().set(key, JSONObject.toJSONString(finalUser), 10, TimeUnit.MINUTES);
            });
        }

        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        // TODO: 粉丝和关注暂未实现
        userDTO.setFansCount(0).setFollowsCount(0);

        return userDTO;
    }

    // 检查redis中的验证码
    private void checkCaptcha(String captcha, String email) {
        String key = CaptchaConstant.REGISTER_EMAIL_Captcha + email;
        // 检查redis中的验证码
        // 如果不存在
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            throw new RegisterException(MessageConstant.CAPTCHA_NOT_SEND_OR_EXPIRED);
        }

        String captcha2 = (String) redisTemplate.opsForValue().get(key);
        if (!captcha.equals(captcha2)) {
            throw new RegisterException(MessageConstant.CAPTCHA_ERROR);
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
            throw new RegisterException(MessageConstant.CAPTCHA_VERIFICATION_FAILED);
        }
    }
}
