package edu.sustech.resource.listener;

import cn.hutool.core.util.StrUtil;
import edu.sustech.resource.service.VodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 资源操作监听器
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-30 6:00
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ResourceOperationListener {

    private final VodService vodService;

    /**
     * 监听删除视频消息
     *
     * @param videoIds 视频id列表, 逗号分隔
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "resource.video.remove.queue", durable = "true"),
            exchange = @Exchange(name = "resource.direct"),
            key = "resource.video.remove"
    ))
    public void listenVideoRemove(String videoIds) {
        log.info("收到删除视频消息: {}", videoIds);
        if (StrUtil.isNotBlank(videoIds)) {
            vodService.removeVideo(videoIds);
        }
    }
}
