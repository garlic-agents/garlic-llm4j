package com.garlic.agents.llm.core;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.garlic.agents.llm.domain.ModelRequest;
import com.garlic.agents.llm.domain.ModelResponse;
import com.garlic.agents.llm.domain.request.MessageContent;
import com.garlic.agents.llm.domain.request.ModelMessage;
import com.garlic.agents.llm.enums.ProcessorType;
import com.garlic.agents.llm.enums.ResponseStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AIModelServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AIModelServiceTest.class);

    protected static String OPENAI_API_URL;
    protected static String OPENAI_API_KEY;

    private static String SUCCESS_RESPONSE_STR;

    @BeforeAll
    public static void setUp() {
        // get config from env
        OPENAI_API_URL = System.getenv("OPENAI_API_URL");
        OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
        Assertions.assertNotNull(OPENAI_API_URL);
        Assertions.assertNotNull(OPENAI_API_KEY);

        // 拼接一个返回字符串用于断言检测
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 50; i++) {
            sb.append(i);
            if (i != 50) {
                sb.append("、");
            }
        }
        SUCCESS_RESPONSE_STR = sb.toString();
    }

    private ModelProcessor getOpenaiProcessor(String url, String apiKey) {
        ModelConfig modelConfig = ModelConfig.builder()
                .url(url)
                .apiKey(apiKey)
                .build();
        AIModelService service = AIModelService.getInstance(modelConfig);
        service.clearProcessors();
        ModelProcessor processor = service.getProcessor(ProcessorType.OPENAI);
        Assertions.assertNotNull(processor);
        return processor;
    }

    private ModelRequest buildOpenaiRequest(String userMessage) {
        MessageContent messageContent = MessageContent.text(userMessage);
        ModelMessage modelMessage = ModelMessage.user(List.of(messageContent));
        return ModelRequest.chat("gpt-3.5-turbo", List.of(modelMessage));
    }

    @Test
    @DisplayName("Test Openai Processor")
    public void testOpenaiProcessor() {
        ModelProcessor processor = getOpenaiProcessor(OPENAI_API_URL, OPENAI_API_KEY);
        ModelRequest request = buildOpenaiRequest("请返回内容“TEST”这四个字母，请不要返回其他任何内容。");
        ModelResponse process = processor.process(request);
        logger.info("process: {}", JSON.toJSONString(process));
        Assertions.assertNotNull(process);
        Assertions.assertEquals(ResponseStatus.SUCCESS, process.getStatus());
        Assertions.assertNotNull(process.getContent());
    }

    @Test
    @DisplayName("Test Openai Stream Processor")
    public void testOpenaiStreamProcessor() {
        ModelProcessor processor = getOpenaiProcessor(OPENAI_API_URL, OPENAI_API_KEY);
        String userMessage = "请返回1到50的阿拉伯数字，中间使用中文顿号“、”分割，请不要返回其他任何内容。";
        ModelRequest request = buildOpenaiRequest(userMessage);
        StringBuilder resultSb = new StringBuilder();
        AtomicBoolean endFlag = new AtomicBoolean(false);
        processor.streamProcess(request, (modelResponse) -> {
            logger.info("stream process callback => {}", JSON.toJSONString(modelResponse));
            Assertions.assertNotEquals(ResponseStatus.FAILURE, modelResponse.getStatus());
            if (modelResponse.getStatus() == ResponseStatus.END) {
                endFlag.set(true);
                return;
            }
            String content = modelResponse.getContent();
            resultSb.append(StrUtil.isEmpty(content) ? "" : content);
        });

        // 等待异步处理完成
        while (!endFlag.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Thread sleep error", e);
            }
        }

        Assertions.assertTrue(StrUtil.contains(resultSb.toString(), SUCCESS_RESPONSE_STR));
    }

    @Test
    @DisplayName("Test Openai Error Processor")
    public void testOpenaiErrorProcessor() {
        ModelProcessor processor = getOpenaiProcessor(OPENAI_API_URL, "error_key");
        String userMessage = "请返回内容“TEST”这四个字母，请不要返回其他任何内容。";
        ModelRequest request = buildOpenaiRequest(userMessage);
        ModelResponse process = processor.process(request);
        logger.info("process response => {}", JSON.toJSONString(process));
        Assertions.assertNotNull(process);
        Assertions.assertEquals(ResponseStatus.FAILURE, process.getStatus());
        Assertions.assertNotNull(process.getContent());
    }

    @Test
    @DisplayName("Test Openai Stream Error Processor")
    public void testOpenaiStreamErrorProcessor() {
        ModelProcessor processor = getOpenaiProcessor(OPENAI_API_URL, "error_key");
        String userMessage = "请返回1到50的阿拉伯数字，中间使用中文顿号“、”分割，请不要返回其他任何内容。";
        ModelRequest request = buildOpenaiRequest(userMessage);
        processor.streamProcess(request, (modelResponse) -> {
            logger.info("stream process callback => {}", JSON.toJSONString(modelResponse));
            Assertions.assertEquals(ResponseStatus.FAILURE, modelResponse.getStatus());
        });
    }

}
