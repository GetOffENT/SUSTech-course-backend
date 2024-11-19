package edu.sustech.resource.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 23:43
 */
@Component
@ConfigurationProperties(prefix = "aliyun.vod")
@Data
public class AliVodProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String regionId;
    private String endpoint;
    private String templateGroupId;
}
