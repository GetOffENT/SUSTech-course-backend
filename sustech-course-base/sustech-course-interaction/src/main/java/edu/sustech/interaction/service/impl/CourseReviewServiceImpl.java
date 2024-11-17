package edu.sustech.interaction.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.result.Result;
import edu.sustech.common.result.ResultCode;
import edu.sustech.interaction.entity.CourseReview;
import edu.sustech.interaction.entity.CourseReviewScore;
import edu.sustech.interaction.entity.vo.CourseReviewScoreVO;
import edu.sustech.interaction.entity.vo.CourseReviewVO;
import edu.sustech.interaction.mapper.CourseReviewLoveMapper;
import edu.sustech.interaction.mapper.CourseReviewMapper;
import edu.sustech.interaction.mapper.CourseReviewScoreMapper;
import edu.sustech.interaction.service.CourseReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 课程评价 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-17
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseReviewServiceImpl extends ServiceImpl<CourseReviewMapper, CourseReview> implements CourseReviewService {

    private final CourseReviewLoveMapper courseReviewLoveMapper;

    private final CourseReviewScoreMapper courseReviewScoreMapper;

    private final UserClient userClient;

    /**
     * 获取课程评价列表
     *
     * @param courseId 课程id
     * @param page     页码
     * @param pageSize 每页大小
     * @return 课程评价列表
     */
    @Override
    public Map<String, Object> getCourseReviewList(Integer courseId, Integer page, Integer pageSize) {

        Page<CourseReview> iPage = new Page<>(page, pageSize);

        Page<CourseReview> courseReviewPage = baseMapper.selectPage(iPage,
                new LambdaQueryWrapper<CourseReview>()
                        .eq(CourseReview::getCourseId, courseId)
        );
        List<CourseReview> courseReviews = courseReviewPage.getRecords();
        List<CourseReviewVO> courseReviewVOS = BeanUtil.copyToList(courseReviews, CourseReviewVO.class);


        // 先根据获取所有的具体指标评分，再在代码中处理逻辑
        List<CourseReviewScore> courseReviewScores = courseReviewScoreMapper.selectList(
                new LambdaQueryWrapper<CourseReviewScore>()
                        .eq(CourseReviewScore::getCourseId, courseId)
        );
        courseReviewScores.sort((o1, o2) -> {
            String index1 = o1.getIndex();
            String index2 = o2.getIndex();
            if (index1.length() != index2.length()) {
                return index1.length() - index2.length();
            } else {
                return index1.compareTo(index2);
            }
        });

        Double totalScore = 0.0;
        for (CourseReviewVO courseReviewVO : courseReviewVOS) {
            // 获取发布用户信息
            Result<UserDTO> userAndCoursesById = userClient.getUserAndCoursesById(courseReviewVO.getUserId());
            if (Objects.equals(userAndCoursesById.getCode(), ResultCode.SUCCESS.code())) {
                courseReviewVO.setUser(userAndCoursesById.getData());
            }

            // 计算总分
            totalScore += courseReviewVO.getScore();

            // 获取具体指标评分
            List<CourseReviewScoreVO> courseReviewScoreVOS = new ArrayList<>();
            courseReviewScores.forEach(courseReviewScore -> {
                if (courseReviewScore.getReviewId().equals(courseReviewVO.getId())) {
                    courseReviewScoreVOS.add(BeanUtil.copyProperties(courseReviewScore, CourseReviewScoreVO.class));
                }
            });
            courseReviewVO.setIndexScores(courseReviewScoreVOS);
        }
        Double score = courseReviewVOS.isEmpty() ? 0 : Double.parseDouble(totalScore.toString()) / courseReviewVOS.size();

        return Map.of(
                "total", courseReviewPage.getTotal(),
                "score", score,
                "reviews", courseReviewVOS
        );
    }
}
