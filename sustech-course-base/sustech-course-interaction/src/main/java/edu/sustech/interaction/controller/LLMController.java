package edu.sustech.interaction.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static cn.hutool.core.lang.Console.log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@RestController
@RequestMapping("/interaction/llm")
@Slf4j
@Api(tags = "大模型相关接口")
@RequiredArgsConstructor
public class LLMController {

    private static final String API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-4.0-turbo-8k-latest";
    private static final String ACCESS_TOKEN = "24.2888665733ea543cbc02f6f6a81ac10e.2592000.1735795690.282335-116499673"; // 替换为实际的access token
    /**
     * 调用大模型回复
     *
     * @param input 问题
     * @return 回答
     */
    @GetMapping("/reply")
    @ApiOperation("调用大模型对问题进行回答")
    public String reply(@RequestParam String input) {
        try {
            System.out.println("Question: " + input);
            // 构造请求的完整URL
            String requestUrl = API_URL + "?access_token=" + ACCESS_TOKEN;

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", input);

            Map<String, Object> payload = new HashMap<>();
            payload.put("messages", List.of(message));

            // 序列化为 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(payload);

            // 构造 HTTP 请求
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(requestUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 发送请求并处理响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 打印响应
            if (response.statusCode() == 200) {
                System.out.println("Response: " + response.body());
                return response.body();
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }
}