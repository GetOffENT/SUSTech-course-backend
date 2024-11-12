package edu.sustech.course.service;

import edu.sustech.course.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.course.entity.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-12
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取全部分类
     * @return 全部分类
     */
    List<CategoryVO> getAllCategories();

    /**
     * 根据id查询对应分类信息
     * @param mcId 主分类ID
     * @param scId 子分类ID
     * @return Category类信息
     */
    Category getCategoryById(String mcId, String scId);
}
