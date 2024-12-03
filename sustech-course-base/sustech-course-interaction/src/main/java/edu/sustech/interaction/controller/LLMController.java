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
import java.util.Map;
import java.util.HashMap;
@RestController
@RequestMapping("/interaction/llm")
@Slf4j
@Api(tags = "大模型相关接口")
@RequiredArgsConstructor
public class LLMController {

    /**
     * 调用大模型回复
     *
     * @param question 问题
     * @return 回答
     */
    @GetMapping("/reply")
    @ApiOperation("调用大模型对问题进行回答")
    public String reply(String question) throws IOException, InterruptedException {
        String API_KEY = "";
        String SECRET_KEY = "";
        String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials";
        String url = TOKEN_URL + "&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
        HttpClient client1 = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper1 = new ObjectMapper();
        var jsonNode = mapper1.readTree(response1.body());
        String accessToken = jsonNode.get("access_token").asText();



        String API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-4.0-turbo-128k";
        HttpClient client2 = HttpClient.newHttpClient();

        // Request Body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", new Object[] {
                Map.of("role", "user", "content", question)
        });
        ObjectMapper mapper2 = new ObjectMapper();
        String jsonBody = mapper2.writeValueAsString(requestBody);

        // Build HTTP Request
        String urlWithToken = API_URL + "?access_token=" + accessToken;
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(urlWithToken))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Send Request
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
        return response2.body();
    }
}
