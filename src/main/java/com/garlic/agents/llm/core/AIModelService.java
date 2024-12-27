package com.garlic.agents.llm.core;

import com.garlic.agents.llm.enums.ModelType;
import com.garlic.agents.llm.exception.AIModelException;
import com.garlic.agents.llm.providers.gemini.GeminiProcessor;
import com.garlic.agents.llm.providers.openai.OpenAIProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * AI model service.
 *
 * @author MoChenYa
 * @date 2024/12/27
 * @since 1.0
 */
public class AIModelService {

    private static volatile AIModelService instance;
    private final Map<ModelType, ModelProcessor> processors = new HashMap<>();
    private ModelConfig config;

    private AIModelService() {
    }

    public static AIModelService getInstance() {
        if (instance == null) {
            synchronized (AIModelService.class) {
                if (instance == null) {
                    instance = new AIModelService();
                }
            }
        }
        return instance;
    }

    public void init(ModelConfig config) {
        this.config = config;
    }

    private ModelProcessor getProcessor(ModelType type) {
        return processors.computeIfAbsent(type, t -> {
            switch (t) {
                case OPENAI:
                    return new OpenAIProcessor(config);
                case GEMINI:
                    return new GeminiProcessor(config);
                default:
                    throw new AIModelException("Unsupported model type: " + t);
            }
        });
    }
}
