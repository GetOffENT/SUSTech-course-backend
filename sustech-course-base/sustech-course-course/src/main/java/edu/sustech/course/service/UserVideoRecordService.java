package edu.sustech.course.service;

import edu.sustech.course.entity.UserVideoRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.course.entity.dto.UserVideoRecordDTO;

/**
 * <p>
 * 用户观看记录 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-14
 */
public interface UserVideoRecordService extends IService<UserVideoRecord> {

    /**
     * 添加观看记录
     * @param recordJSON 观看记录对应JSON
     */
    void addRecord(String recordJSON);
}
