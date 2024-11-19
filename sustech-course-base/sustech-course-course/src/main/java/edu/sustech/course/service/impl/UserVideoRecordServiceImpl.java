package edu.sustech.course.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.course.entity.UserVideoRecord;
import edu.sustech.course.entity.dto.UserVideoRecordDTO;
import edu.sustech.course.mapper.UserVideoRecordMapper;
import edu.sustech.course.service.UserVideoRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    /**
     * 添加观看记录
     *
     * @param recordJSON 观看记录对应JSON
     */
    @Override
    public void addRecord(String recordJSON) {
        UserVideoRecordDTO userVideoRecordDTO = JSONObject.parseObject(recordJSON, UserVideoRecordDTO.class);
        List<Integer> timePoints = userVideoRecordDTO.getTimePoints();

        UserVideoRecord userVideoRecord = baseMapper.selectOne(
                new LambdaQueryWrapper<UserVideoRecord>()
                        .eq(UserVideoRecord::getUserId, userVideoRecordDTO.getUserId())
                        .eq(UserVideoRecord::getVideoId, userVideoRecordDTO.getVideoId())
        );

        // 查询已有的记录并合并时间点
        if (userVideoRecord != null) {
            timePoints.addAll(StrUtil.split(userVideoRecord.getPlayRange(), ',').stream()
                    .map(Integer::parseInt).toList());
        }
        List<Integer> playRange = mergeWatchIntervals(timePoints);

        // 计算新的总观看时间
        double totalPlayTime = 0.0;
        for (int i = 0; i < playRange.size(); i += 2) {
            totalPlayTime += playRange.get(i + 1) - playRange.get(i);
        }

        if (userVideoRecord == null) {
            userVideoRecord = BeanUtil.copyProperties(userVideoRecordDTO, UserVideoRecord.class);
            userVideoRecord.setPlayRange(CollUtil.join(playRange, ","))
                    .setTotalPlayTime(totalPlayTime)
                    .setIsLearned((byte) (totalPlayTime >= userVideoRecordDTO.getMinWatchTime() ? 1 : 0));
            baseMapper.insert(userVideoRecord);
        } else {
            userVideoRecord.setPlayTime(userVideoRecordDTO.getPlayTime())
                    .setPlayRange(CollUtil.join(playRange, ","))
                    .setTotalPlayTime(userVideoRecord.getTotalPlayTime());
            if (userVideoRecord.getIsLearned() == 0) {
                userVideoRecord.setIsLearned((byte) (totalPlayTime >= userVideoRecordDTO.getMinWatchTime() ? 1 : 0));
            }
            baseMapper.updateById(userVideoRecord);
        }
    }


    /**
     * 合并观看时间区间
     *
     * @param timePoints 输入的时间点列表，两两一组表示区间
     * @return 合并后的区间列表
     */
    public List<Integer> mergeWatchIntervals(List<Integer> timePoints) {
        if (timePoints == null || timePoints.size() % 2 != 0) {
            throw new IllegalArgumentException(MessageConstant.INVALID_TIME_POINTS);
        }

        // 将时间点两两分组为区间
        List<int[]> intervals = new ArrayList<>();
        for (int i = 0; i < timePoints.size(); i += 2) {
            int start = timePoints.get(i);
            int end = timePoints.get(i + 1);
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            intervals.add(new int[]{start, end});
        }

        // 按区间的开始时间排序
        intervals.sort(Comparator.comparingInt(a -> a[0]));

        // 合并区间
        List<int[]> mergedIntervals = new ArrayList<>();
        int[] currentInterval = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++) {
            int[] nextInterval = intervals.get(i);
            if (currentInterval[1] >= nextInterval[0]) { // 有重叠或连接
                currentInterval[1] = Math.max(currentInterval[1], nextInterval[1]);
            } else { // 无重叠，加入结果集
                mergedIntervals.add(currentInterval);
                currentInterval = nextInterval;
            }
        }
        // 添加最后一个区间
        mergedIntervals.add(currentInterval);

        List<Integer> result = new ArrayList<>();
        for (int[] interval : mergedIntervals) {
            result.add(interval[0]);
            result.add(interval[1]);
        }
        return result;
    }
}
