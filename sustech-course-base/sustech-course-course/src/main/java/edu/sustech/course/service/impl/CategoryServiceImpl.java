package edu.sustech.course.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.course.entity.Category;
import edu.sustech.course.entity.vo.CategoryVO;
import edu.sustech.course.mapper.CategoryMapper;
import edu.sustech.course.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 获取全部分类
     *
     * @return 全部分类
     */
    @Override
    public List<CategoryVO> getAllCategories() {
        List<CategoryVO> categoryVOList = null;
        // TODO: 尝试从redis中获取数据

        List<Category> categoryList = baseMapper.selectList(null);

        Map<String, CategoryVO> tempCategoryVOMap = new HashMap<>();

        for (Category category : categoryList) {

            if (StrUtil.isNotBlank(category.getRcmTag())) {
                List<String> tags = StrUtil.split(category.getRcmTag(), '\n');
            }

            // 整合主分类
            if (!tempCategoryVOMap.containsKey(category.getMcId())) {
                tempCategoryVOMap.put(category.getMcId(),
                        CategoryVO.builder()
                                .mcId(category.getMcId())
                                .mcName(category.getMcName())
                                .scList(new ArrayList<>())
                                .build());
            }

            // 整合子分类
            tempCategoryVOMap.get(category.getMcId()).getScList().add(
                    Map.of(
                            "id", category.getScId(),
                            "mcId", category.getMcId(),
                            "scId", category.getScId(),
                            "scName", category.getScName(),
                            "descr", category.getDescription(),
                            "rcmTag", category.getRcmTag(),
                            "sort", category.getSort()
                    ));
        }

        // 排序 主分区根据其下子分区第一个元素的sort字段排序，子分区根据sort字段排序
        tempCategoryVOMap.values().forEach(categoryVO -> {
            categoryVO.getScList().sort(Comparator.comparingInt(o -> (int) o.get("sort")));
        });

        // 主分区排序
        categoryVOList = new ArrayList<>(tempCategoryVOMap.values());
        categoryVOList.sort(Comparator.comparingInt(o -> (int) o.getScList().get(0).get("sort")));
        return categoryVOList;
    }

    /**
     * 根据id查询对应分类信息
     *
     * @param mcId 主分类ID
     * @param scId 子分类ID
     * @return Category类信息
     */
    @Override
    public Category getCategoryById(String mcId, String scId) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getMcId, mcId)
                        .eq(Category::getScId, scId)
        );
    }
}
