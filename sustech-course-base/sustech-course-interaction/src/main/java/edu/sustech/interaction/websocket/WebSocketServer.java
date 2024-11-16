package edu.sustech.interaction.websocket;

import com.alibaba.fastjson.JSONObject;
import edu.sustech.api.client.CourseClient;
import edu.sustech.interaction.entity.Danmu;
import edu.sustech.interaction.mapper.DanmuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-13 15:42
 */
@Component
@Slf4j
@ServerEndpoint("/ws/danmu/{vid}")
public class WebSocketServer {
    // 对每个视频存储该视频下的session集合
    private static final Map<String, Set<Session>> videoConnectionMap = new ConcurrentHashMap<>();

    // 存储每个Session对应的userId
    private static final Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();

    private static DanmuMapper danmuMapper;

    private static CourseClient courseClient;

    @Autowired
    public void setDanmuMapper(DanmuMapper danmuMapper, CourseClient courseClient) {
        WebSocketServer.danmuMapper = danmuMapper;
        WebSocketServer.courseClient = courseClient;
    }

    /**
     * 连接建立时触发，记录session到map
     *
     * @param session 会话
     * @param vid     视频的ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("vid") String vid) {
        if (videoConnectionMap.get(vid) == null) {
            Set<Session> set = new HashSet<>();
            set.add(session);
            videoConnectionMap.put(vid, set);
        } else {
            videoConnectionMap.get(vid).add(session);
        }

        // 提取Session queryString中的userId
        String userId = null;
        String queryString = session.getQueryString();
        if (queryString != null) {
            userId = queryString.split("=")[1];
            if (userId != null) {
                sessionUserMap.put(session, Long.parseLong(userId));
            }
        }

        sendMessage(vid, "当前观看人数" + videoConnectionMap.get(vid).size());
        log.info("用户：【{}】正在客户端：【{}】 观看视频：【{}】", userId == null ? "游客" : userId, session, vid);
    }

    /**
     * 收到消息时触发，记录到数据库并转发到对应的全部连接
     *
     * @param session 当前会话
     * @param message 信息体（包含"token"、"vid"、"data"字段）
     * @param vid     视频ID
     */
    @OnMessage
    public void onMessage(Session session, String message, @PathParam("vid") String vid) {
        try {
            Long userId = sessionUserMap.get(session);
            if (userId == null) {
                session.getBasicRemote().sendText("请先登录");
            }

            JSONObject danmuData = JSONObject.parseObject(message);
            Danmu danmu = Danmu.builder()
                    .videoId(Long.parseLong(vid))
                    .userId(userId)
                    .content(danmuData.getString("content"))
                    .fontsize(danmuData.getByte("fontsize"))
                    .mode(danmuData.getByte("mode"))
                    .color(danmuData.getString("color"))
                    .timePoint(danmuData.getDouble("timePoint"))
                    .state((byte) 1)
                    .isDelete((byte) 0)
                    .build();
            int count = danmuMapper.insert(danmu);
            courseClient.updateDanmuCount(Long.parseLong(vid), count);
            sendMessage(vid, JSONObject.toJSONString(danmu));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭时执行
     *
     * @param session 当前会话
     * @param vid     视频ID
     */
    @OnClose
    public void onClose(Session session, @PathParam("vid") String vid) {
        Long userId = sessionUserMap.get(session);
        log.info("用户: 【{}】 客户端：【{}】 已结束观看视频：【{}】", userId == null ? "游客" : userId, session, vid);
        // 从缓存中移除连接记录
        videoConnectionMap.get(vid).remove(session);
        if (videoConnectionMap.get(vid).isEmpty()) {
            // 如果没人了就直接移除这个视频
            videoConnectionMap.remove(vid);
        } else {
            // 否则更新在线人数
            sendMessage(vid, "当前观看人数" + videoConnectionMap.get(vid).size());
        }

        // 移除session对应的userId
        sessionUserMap.remove(session);
//        System.out.println("关闭连接，当前观看人数: " + videoConnectionMap.get(vid).size());
    }

    @OnError
    public void onError(Throwable error) {
        log.error("websocket发生错误");
        error.printStackTrace();
    }

    /**
     * 往对应的全部连接发送消息
     *
     * @param vid  视频ID
     * @param text 消息内容，对象需转成JSON字符串
     */
    public void sendMessage(String vid, String text) {
        Set<Session> set = videoConnectionMap.get(vid);
        // 使用并行流往各客户端发送数据
        set.parallelStream().forEach(session -> {
            try {
                session.getBasicRemote().sendText(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
