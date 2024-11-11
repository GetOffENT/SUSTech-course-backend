package edu.sustech.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.admin.entity.Carousel;
import edu.sustech.admin.entity.vo.CarouselAdminVO;
import edu.sustech.admin.entity.vo.CarouselVO;
import edu.sustech.admin.mapper.CarouselMapper;
import edu.sustech.admin.service.CarouselService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.common.constant.MessageConstant;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 轮播图表 服务实现类
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-10
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {

    /**
     * 获取需要展示的轮播图列表
     *
     * @return 轮播图列表
     */
    @Override
    public List<CarouselVO> getCarouselList() {
        List<Carousel> carouselList = this.list(
                new LambdaQueryWrapper<Carousel>()
                        .ne(Carousel::getSort, 0)
                        .orderByAsc(Carousel::getSort)
        );
        return BeanUtil.copyToList(carouselList, CarouselVO.class);
    }

    /**
     * 获取所有轮播图列表
     *
     * @return
     */
    @Override
    public List<CarouselAdminVO> getAllCarouselList() {
        List<Carousel> carouselList = this.list();
        return BeanUtil.copyToList(carouselList, CarouselAdminVO.class);
    }

    /**
     * 改变轮播图展示状态
     *
     * @param id 轮播图id
     */
    @Override
    public Carousel showOrHide(Long id) {
        Carousel carousel = this.getById(id);

        if (carousel == null) {
            throw new RuntimeException(MessageConstant.ERROR);
        }

        if (carousel.getSort() != 0) {
            carousel.setSort(0);
            this.updateById(carousel);
            return carousel;
        }

        List<Carousel> carouselList = this.list();
        Carousel carouselMax = carouselList.stream()
                .max(Comparator.comparingInt(Carousel::getSort))
                .orElse(null);
        if (carouselMax == null) {
            carousel.setSort(1);
            this.updateById(carousel);
            return carousel;
        }

        carousel.setSort(carouselMax.getSort() + 1);
        this.updateById(carousel);
        return carousel;
    }
}
