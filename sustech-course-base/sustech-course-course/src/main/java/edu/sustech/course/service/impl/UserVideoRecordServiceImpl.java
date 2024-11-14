package edu.sustech.course.service.impl;

import edu.sustech.course.entity.UserVideoRecord;
import edu.sustech.course.mapper.UserVideoRecordMapper;
import edu.sustech.course.service.UserVideoRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户观看记录 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
@Service
public class UserVideoRecordServiceImpl extends ServiceImpl<UserVideoRecordMapper, UserVideoRecord> implements UserVideoRecordService {

}
