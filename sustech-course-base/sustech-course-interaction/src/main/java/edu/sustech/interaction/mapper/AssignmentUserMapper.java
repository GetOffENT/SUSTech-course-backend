package edu.sustech.interaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.interaction.entity.AssignmentUser;

import java.util.List;

/**
 * <p>
 * 作业学生关系表 Mapper 接口
 * </p>
 *
 * @author Yuxian Wu
 * @since 2024-12-02
 */
public interface AssignmentUserMapper extends BaseMapper<AssignmentUser> {

    /**
     * 批量插入
     * @param assignmentUserList 作业记录列表
     * @return 插入数量
     */
    int insertBatch(List<AssignmentUser> assignmentUserList);
}
