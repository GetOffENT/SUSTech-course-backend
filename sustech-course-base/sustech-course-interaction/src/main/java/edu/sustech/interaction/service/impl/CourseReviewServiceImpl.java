package edu.sustech.interaction.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseReviewException;
import edu.sustech.common.result.Result;
import edu.sustech.common.enums.ResultCode;
import edu.sustech.common.util.UserContext;
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
import org.springframework.transaction.annotation.Transactional;

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
     * @return 课程评价列表(不包括自己的评论)
     */
    @Override
    public Map<String, Object> getCourseReviewList(Integer courseId, Integer page, Integer pageSize) {

        Page<CourseReview> iPage = new Page<>(page, pageSize);

        // 获取课程的平均分（不能使用分页获取课程平均分）
        Double score = baseMapper.selectAverageScore(courseId);

        // 如果登录了就不查自己的评论，因为自己的评论会单独获取
        Long userId = UserContext.getUser();
        LambdaQueryWrapper<CourseReview> queryWrapper = new LambdaQueryWrapper<CourseReview>()
                .eq(CourseReview::getCourseId, courseId);
        if (userId != null) {
            queryWrapper.ne(CourseReview::getUserId, userId);
        }
        Page<CourseReview> courseReviewPage = baseMapper.selectPage(iPage, queryWrapper);
        List<CourseReview> courseReviews = courseReviewPage.getRecords();
        List<CourseReviewVO> courseReviewVOS = BeanUtil.copyToList(courseReviews, CourseReviewVO.class);

        // 先根据获取所有的具体指标评分，再在代码中处理逻辑
        List<CourseReviewScore> courseReviewScores = courseReviewScoreMapper.selectList(
                new LambdaQueryWrapper<CourseReviewScore>()
                        .eq(CourseReviewScore::getCourseId, courseId)
        );

        if (CollUtil.isEmpty(courseReviewScores)) {
            return Map.of(
                    "total", courseReviewPage.getTotal(),
                    "score", score,
                    "reviews", courseReviewVOS
            );
        }

        courseReviewScores.sort((o1, o2) -> {
            String index1 = o1.getIndex();
            String index2 = o2.getIndex();
            if (index1.length() != index2.length()) {
                return index1.length() - index2.length();
            } else {
                return index1.compareTo(index2);
            }
        });

        for (CourseReviewVO courseReviewVO : courseReviewVOS) {
            // 获取发布用户信息
            Result<UserDTO> userAndCoursesById = userClient.getUserAndCoursesById(courseReviewVO.getUserId());
            if (!Objects.equals(userAndCoursesById.getCode(), ResultCode.SUCCESS.code())) {
                throw new CourseReviewException(userAndCoursesById.getMessage());
            }
            courseReviewVO.setUser(userAndCoursesById.getData());
            // 获取具体指标评分
            List<CourseReviewScoreVO> courseReviewScoreVOS = new ArrayList<>();
            courseReviewScores.forEach(courseReviewScore -> {
                if (courseReviewScore.getReviewId().equals(courseReviewVO.getId())) {
                    courseReviewScoreVOS.add(BeanUtil.copyProperties(courseReviewScore, CourseReviewScoreVO.class));
                }
            });
            courseReviewVO.setIndexScores(courseReviewScoreVOS);
        }

        return Map.of(
                "total", courseReviewPage.getTotal(),
                "score", score,
                "reviews", courseReviewVOS
        );
    }

    /**
     * 添加课程评价
     *
     * @param courseReviewVO 课程评价
     * @param courseId       课程id
     */
    @Transactional
    @Override
    public void addCourseReview(CourseReviewVO courseReviewVO, Long courseId) {
        Long Userid = checkUser();
        CourseReview courseReview = BeanUtil.copyProperties(courseReviewVO, CourseReview.class);
        courseReview.setUserId(Userid).setCourseId(courseId);
        this.save(courseReview);

        List<CourseReviewScoreVO> indexScores = courseReviewVO.getIndexScores();
        if (indexScores == null || indexScores.isEmpty()) {
            throw new CourseReviewException(MessageConstant.PARAM_ERROR);
        }

        for (CourseReviewScoreVO indexScore : indexScores) {
            CourseReviewScore courseReviewScore = BeanUtil.copyProperties(indexScore, CourseReviewScore.class)
                    .setCourseId(courseId).setReviewId(courseReview.getId());
            courseReviewScoreMapper.insert(courseReviewScore);
        }

    }

    /**
     * 获取用户对课程评价
     *
     * @param courseId 课程id
     * @return 课程评价
     */
    @Override
    public CourseReviewVO getCourseReview(Long courseId) {
        Long Userid = checkUser();
        CourseReview courseReview = baseMapper.selectOne(
                new LambdaQueryWrapper<CourseReview>()
                        .eq(CourseReview::getCourseId, courseId)
                        .eq(CourseReview::getUserId, Userid)
        );
        CourseReviewVO courseReviewVO = BeanUtil.copyProperties(courseReview, CourseReviewVO.class);

        if (courseReview == null) {
            return courseReviewVO;
        }

        // 获取具体指标评价
        List<CourseReviewScore> courseReviewScores = courseReviewScoreMapper.selectList(
                new LambdaQueryWrapper<CourseReviewScore>()
                        .eq(CourseReviewScore::getCourseId, courseId)
                        .eq(CourseReviewScore::getReviewId, courseReview.getId())
        );
        List<CourseReviewScoreVO> courseReviewScoreVOS = BeanUtil.copyToList(courseReviewScores, CourseReviewScoreVO.class);
        courseReviewVO.setIndexScores(courseReviewScoreVOS);
        return courseReviewVO;
    }

    /**
     * 删除课程评价
     *
     * @param reviewId 评价id
     */
    @Override
    @Transactional
    public void deleteCourseReview(Long reviewId) {
        int flag = baseMapper.deleteById(reviewId);
        if (flag == 0) {
            throw new CourseReviewException(MessageConstant.ERROR);
        }

        // 删除具体指标评分
        courseReviewScoreMapper.delete(
                new LambdaQueryWrapper<CourseReviewScore>()
                        .eq(CourseReviewScore::getReviewId, reviewId)
        );
    }

    private Long checkUser() {
        Long Userid = UserContext.getUser();
        if (Userid == null) {
            throw new CourseReviewException(MessageConstant.NO_PERMISSION_TO_ADD_COURSE_REVIEW);
        }
        return Userid;
    }
}
