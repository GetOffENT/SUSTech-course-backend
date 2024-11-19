import cn.hutool.core.collection.CollUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-18 21:33
 */
public class TimePointsTest {

    @Test
    public void test() {
        List<Integer> timePoints = List.of(5, 8, 1, 6, 9, 10);

        // 将时间点两两分组为区间
        List<int[]> intervals = new ArrayList<>();
        for (int i = 0; i < timePoints.size(); i += 2) {
            int start = timePoints.get(i);
            int end = timePoints.get(i + 1);
            if (start > end) { // 如果开始时间大于结束时间，交换
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

        // 转换为最终结果
        List<Integer> result = new ArrayList<>();
        for (int[] interval : mergedIntervals) {
            result.add(interval[0]);
            result.add(interval[1]);
        }
        System.out.println(result);

        System.out.println(CollUtil.join(result, ","));
    }
}
