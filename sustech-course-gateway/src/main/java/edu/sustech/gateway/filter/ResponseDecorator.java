package edu.sustech.gateway.filter;

import cn.hutool.json.JSONObject;
import edu.sustech.gateway.properties.JwtProperties;
import edu.sustech.gateway.util.JwtTool;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * <p>
 * 响应装饰器（重构响应体）
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-15 6:10
 */
public class ResponseDecorator extends ServerHttpResponseDecorator {

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    public ResponseDecorator(ServerHttpResponse delegate, JwtTool jwtTool, JwtProperties jwtProperties) {
        super(delegate);
        this.jwtTool = jwtTool;
        this.jwtProperties = jwtProperties;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

        if (body instanceof Flux) {
            Flux<DataBuffer> fluxBody = (Flux<DataBuffer>) body;

            return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                DataBuffer join = dataBufferFactory.join(dataBuffers);

                byte[] content = new byte[join.readableByteCount()];
                join.read(content);
                DataBufferUtils.release(join);// 释放掉内存

                String bodyStr = new String(content, Charset.forName("UTF-8"));

                //修改响应体
                bodyStr = modifyBody(bodyStr);

                getDelegate().getHeaders().setContentLength(bodyStr.getBytes().length);
                return bufferFactory().wrap(bodyStr.getBytes());
            }));
        }
        return super.writeWith(body);
    }

    //重写这个函数即可
    private String modifyBody(String jsonStr) {
        JSONObject json = new JSONObject(jsonStr);
        //TODO...修改响应体

        JSONObject data = json.getJSONObject("data");

        if (data == null) {
            return json.toString();
        }

        Long id = Long.parseLong(String.valueOf(data.getJSONObject("user").get("id")));
        String token = jwtTool.createToken(id, jwtProperties.getTokenTTL());


        data.set("token", token);

        return json.toString();
    }
}
