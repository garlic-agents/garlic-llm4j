package com.garlic.agents.llm.processors.openai;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.garlic.agents.llm.core.ModelConfig;
import com.garlic.agents.llm.core.ModelProcessor;
import com.garlic.agents.llm.domain.ModelRequest;
import com.garlic.agents.llm.domain.ModelResponse;
import com.garlic.agents.llm.domain.request.MessageContent;
import com.garlic.agents.llm.domain.request.ModelMessage;
import com.garlic.agents.llm.domain.response.ModelUsage;
import com.garlic.agents.llm.domain.response.StreamCallback;
import com.garlic.agents.llm.enums.ResponseStatus;
import com.garlic.agents.llm.utils.HttpUtil;
import com.garlic.agents.llm.utils.JsonUtil;
import com.garlic.agents.llm.utils.domain.HttpSseResponse;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * openai processor
 *
 * @author MoChenYa
 * @since 1.0
 */
public class OpenAIProcessor implements ModelProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIProcessor.class);

    public static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json");

    private final ModelConfig config;

    public OpenAIProcessor(ModelConfig config) {
        this.config = config;
    }

    @Override
    public ModelResponse process(ModelRequest request) {
        Request httpRequest = buildRequest(request, false);
        Response httpResponse = HttpUtil.request(httpRequest);
        return buildResponse(httpResponse);
    }

    private ModelResponse buildResponse(Response httpResponse) {
        try {
            assert httpResponse.body() != null;
            String string = httpResponse.body().string();
            logger.debug("response => {}", string);
            DocumentContext documentContext = JsonPath.parse(string);
            String content = JsonUtil.safeRead(documentContext, "$.choices[0].message.content", String.class);
            if (ObjUtil.isNull(content)) {
                String error = JsonUtil.safeRead(documentContext, "$.error", String.class);
                if (StrUtil.isNotBlank(error)) {
                    return new ModelResponse(ResponseStatus.FAILURE, error);
                }
                return new ModelResponse(ResponseStatus.FAILURE, string);
            }
            ModelUsage modelUsage = new ModelUsage.Builder()
                    .promptTokens(JsonUtil.safeRead(documentContext, "$.usage.prompt_tokens", Long.class))
                    .completionTokens(JsonUtil.safeRead(documentContext, "$.usage.prompt_tokens", Long.class))
                    .totalTokens(JsonUtil.safeRead(documentContext, "$.usage.total_tokens", Long.class))
                    .build();
            return new ModelResponse.Builder()
                    .status(ResponseStatus.SUCCESS)
                    .content(content)
                    .usage(modelUsage)
                    .build();
        } catch (IOException e) {
            return new ModelResponse(ResponseStatus.FAILURE, e.getMessage());
        }
    }

    @Override
    public void streamProcess(ModelRequest request, StreamCallback callback) {
        Request httpRequest = buildRequest(request, true);
        HttpUtil.streamRequest(httpRequest, (response) -> {
            logger.debug("status => {} response => {}", response.getStatus(), response.getData());
            ModelResponse modelResponse = buildStreamResponse(response);
            if (ObjUtil.isNull(modelResponse)) {
                return;
            }
            logger.debug("model response => {}", JSON.toJSONString(modelResponse));
            callback.accept(modelResponse);
        });
    }

    private ModelResponse buildStreamResponse(HttpSseResponse response) {
        ResponseStatus status = response.getStatus();
        String data = response.getData();
        if (status != ResponseStatus.SUCCESS) {
            return new ModelResponse(status, data);
        }
        if ("[DONE]".equals(data) || StrUtil.isBlank(data)) {
            return null;
        }
        if (!JSON.isValid(data)) {
            logger.error("Invalid json response => {}", data);
            return new ModelResponse(ResponseStatus.FAILURE, "Invalid json response => " + data);
        }
        DocumentContext documentContext = JsonPath.parse(data);
        String content = JsonUtil.safeRead(documentContext, "$.choices[0].delta.content", String.class);
        // check if usage parameter exists in response
        ModelUsage modelUsage = null;
        if (JsonUtil.checkJsonPath(documentContext, "$.usage")) {
            modelUsage = new ModelUsage.Builder()
                    .promptTokens(JsonUtil.safeRead(documentContext, "$.usage.prompt_tokens", Long.class))
                    .completionTokens(JsonUtil.safeRead(documentContext, "$.usage.prompt_tokens", Long.class))
                    .totalTokens(JsonUtil.safeRead(documentContext, "$.usage.total_tokens", Long.class))
                    .build();
        }
        return new ModelResponse.Builder()
                .status(ResponseStatus.SUCCESS)
                .content(content)
                .usage(modelUsage)
                .build();
    }

    private Request buildRequest(ModelRequest request, boolean stream) {
        JSONArray openaiMessages = new JSONArray();
        for (ModelMessage message : request.getMessages()) {
            JSONArray openaiContent = new JSONArray();
            List<MessageContent> content = message.getContent();
            // Convert message content to OpenAI format
            for (MessageContent messageContent : content) {
                openaiContent.add(parseMessageContent(messageContent));
            }
            JSONObject openaiMessage = new JSONObject();
            openaiMessage.put("role", message.getRole().parseOpenaiRole());
            openaiMessage.put("content", openaiContent);
            openaiMessages.add(openaiMessage);
        }
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", request.getModelName());
        requestBody.put("stream", stream);
        requestBody.put("messages", openaiMessages);
        RequestBody body = RequestBody.create(requestBody.toJSONString(), JSON_MEDIA_TYPE);
        return new Request.Builder()
                .url(config.getUrl() + "/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + config.getApiKey())
                .post(body)
                .build();
    }

    @NotNull
    private static JSONObject parseMessageContent(MessageContent messageContent) {
        JSONObject openaiMessageContent = new JSONObject();
        switch (messageContent.getType()) {
            case TEXT -> {
                openaiMessageContent.put("type", "text");
                openaiMessageContent.put("text", messageContent.getText());
            }
            case IMAGE -> {
                openaiMessageContent.put("type", "image_url");
                openaiMessageContent.put("image_url", messageContent.getImage());
            }
        }
        return openaiMessageContent;
    }
}
