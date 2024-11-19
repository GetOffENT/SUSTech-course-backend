package edu.sustech.api.client;

import edu.sustech.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-20 2:08
 */
@FeignClient("resource-service")
public interface ResourceClient {

    /**
     * 删除阿里云视频
     *
     * @param id 视频阿里云id
     * @return 删除结果
     */
    @DeleteMapping("/resource/vod/{id}")
    Result<Object> removeAlyVideo(@PathVariable String id);

    /**
     * 批量删除阿里云视频
     *
     * @param videoIdList 视频阿里云id列表
     * @return 删除结果
     */
    @DeleteMapping("/resource/vod/batch")
    Result<Object> removeAlyVideoBatch(@RequestParam List<String> videoIdList);
}
