package com.garlic.agents.llm.providers.openai;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.garlic.agents.llm.core.ModelConfig;
import com.garlic.agents.llm.core.ModelProcessor;
import com.garlic.agents.llm.domain.MessageContent;
import com.garlic.agents.llm.domain.ModelMessage;
import com.garlic.agents.llm.domain.ModelRequest;
import com.garlic.agents.llm.domain.ModelResponse;
import com.garlic.agents.llm.domain.enums.ResponseType;
import com.garlic.agents.llm.utils.HttpUtil;
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

    public static final MediaType JSON = MediaType.get("application/json");

    private final ModelConfig config;

    public OpenAIProcessor(ModelConfig config) {
        this.config = config;
    }

    @Override
    public ModelResponse process(ModelRequest request) {
        Request httpRequest = buildRequest(request);
        Response httpResponse = HttpUtil.request(httpRequest);
        return buildResponse(httpResponse);
    }

    private Request buildRequest(ModelRequest request) {
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
        requestBody.put("model", request.getModel());
        requestBody.put("messages", openaiMessages);
        RequestBody body = RequestBody.create(requestBody.toJSONString(), JSON);
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

    private ModelResponse buildResponse(Response httpResponse) {
        try {
            assert httpResponse.body() != null;
            String string = httpResponse.body().string();
            return new ModelResponse(ResponseType.SUCCESS, string);
        } catch (IOException e) {
            return new ModelResponse(ResponseType.ERROR, e.getMessage());
        }
    }
}
