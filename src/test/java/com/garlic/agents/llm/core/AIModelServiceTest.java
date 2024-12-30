package com.garlic.agents.llm.core;

import com.garlic.agents.llm.enums.ModelType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AIModelServiceTest {

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
    @DisplayName("Test Get Openai Processor")
    public void testGetOpenaiProcessor() {
        AIModelService service = AIModelService.getInstance();
        service.init(new ModelConfig() {
        });
        ModelProcessor processor = service.getProcessor(ModelType.OPENAI);
        Assertions.assertNotNull(processor);
    }

    @Test
    @DisplayName("Test Openai Processor")
    public void testOpenaiProcessor() {
        AIModelService service = AIModelService.getInstance();
        service.init(new ModelConfig() {
        });
        ModelProcessor processor = service.getProcessor(ModelType.OPENAI);
        Assertions.assertNotNull(processor);
    }

}
