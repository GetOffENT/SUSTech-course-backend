package edu.sustech.interaction.mapper;

import edu.sustech.interaction.entity.CommentVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-11-13
 */
public interface CommentVideoMapper extends BaseMapper<CommentVideo> {


    List<Long> selectIdsToDeleteRecursively(Long id);


    Integer deleteBatchByIds(List<Long> commentIds);
}
