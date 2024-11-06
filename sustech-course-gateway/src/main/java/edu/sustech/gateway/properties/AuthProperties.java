package edu.sustech.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-07 6:57
 */
@Data
@ConfigurationProperties(prefix = "sustechcourse.auth")
@Component
public class AuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
