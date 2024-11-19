package edu.sustech.resource.config;

import edu.sustech.resource.utils.AliVodProperties;
import edu.sustech.resource.utils.AliVodUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 配置类，用于创建AliVodUtil对象
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-19 23:39
 */
@Configuration
@Slf4j
public class VodConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliVodUtil aliVodUtil(AliVodProperties aliOssProperties) {
        log.info("开始创建阿里云视频点播工具类对象:{}", aliOssProperties);
        return new AliVodUtil(aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getEndpoint(),
                aliOssProperties.getRegionId(),
                aliOssProperties.getTemplateGroupId());
    }
}
