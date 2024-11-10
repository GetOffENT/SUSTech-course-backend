package edu.sustech.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.admin.entity.Carousel;
import edu.sustech.admin.entity.vo.CarouselVO;
import edu.sustech.admin.mapper.CarouselMapper;
import edu.sustech.admin.service.CarouselService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
