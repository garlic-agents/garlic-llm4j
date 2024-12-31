package com.garlic.agents.llm.core;

import com.alibaba.fastjson2.JSON;
import com.garlic.agents.llm.domain.MessageContent;
import com.garlic.agents.llm.domain.ModelMessage;
import com.garlic.agents.llm.domain.ModelRequest;
import com.garlic.agents.llm.domain.ModelResponse;
import com.garlic.agents.llm.domain.enums.MessageContentType;
import com.garlic.agents.llm.domain.enums.MessageRole;
import com.garlic.agents.llm.domain.enums.ResponseType;
import com.garlic.agents.llm.enums.ModelType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AIModelServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AIModelServiceTest.class);

    protected static String OPENAI_API_URL;
    protected static String OPENAI_API_KEY;

    @BeforeAll
    public static void setUp() {
        // get config from env
        OPENAI_API_URL = System.getenv("OPENAI_API_URL");
        OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
        Assertions.assertNotNull(OPENAI_API_URL);
        Assertions.assertNotNull(OPENAI_API_KEY);
    }

    @Test
    @DisplayName("Test Openai Processor")
    public void testOpenaiProcessor() {
        AIModelService service = AIModelService.getInstance();
        ModelConfig modelConfig = ModelConfig.builder()
                .url(OPENAI_API_URL)
                .apiKey(OPENAI_API_KEY)
                .build();
        service.init(modelConfig);
        ModelProcessor processor = service.getProcessor(ModelType.OPENAI);
        Assertions.assertNotNull(processor);
        MessageContent messageContent = new MessageContent(MessageContentType.TEXT, "pls say hello", null);
        ModelMessage modelMessage = new ModelMessage(MessageRole.USER, List.of(messageContent));
        ModelRequest request = new ModelRequest("gpt-3.5-turbo", List.of(modelMessage));
        ModelResponse process = processor.process(request);
        logger.info("process: {}", JSON.toJSONString(process));
        Assertions.assertNotNull(process);
        Assertions.assertEquals(ResponseType.SUCCESS, process.getType());
        Assertions.assertNotNull(process.getContent());
    }

}
