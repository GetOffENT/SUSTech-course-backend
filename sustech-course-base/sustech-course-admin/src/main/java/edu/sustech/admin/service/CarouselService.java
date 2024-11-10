package edu.sustech.admin.service;

import edu.sustech.admin.entity.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.admin.entity.vo.CarouselVO;

import java.util.List;

/**
 * <p>
 * 轮播图表 服务类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-10
 */
public interface CarouselService extends IService<Carousel> {

    /**
     * 获取需要展示的轮播图列表
     * @return 轮播图列表
     */
    List<CarouselVO> getCarouselList();
}
