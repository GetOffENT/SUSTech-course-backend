package edu.sustech.api.client;

import edu.sustech.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 获取视频播放信息
     *
     * @param videoSourceId 视频源id
     * @return 视频播放地址
     */
    @GetMapping("/resource/vod/play/{videoSourceId}")
    Result<String> getPlayInfo(@PathVariable String videoSourceId);

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件路径
     */
    @PostMapping(value = "/resource/oss/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> upload(@RequestPart("file") MultipartFile file);
}
