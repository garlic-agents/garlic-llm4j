package com.garlic.agents.llm.core;

import cn.hutool.core.util.ObjUtil;
import com.garlic.agents.llm.enums.ModelType;
import com.garlic.agents.llm.exception.AIModelException;
import com.garlic.agents.llm.providers.gemini.GeminiProcessor;
import com.garlic.agents.llm.providers.openai.OpenAIProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI model service.
 *
 * @author MoChenYa
 * @since 1.0
 */
public class AIModelService {

    private static volatile AIModelService instance;
    private final Map<ModelType, ModelProcessor> processors = new ConcurrentHashMap<>();
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

    public ModelProcessor getProcessor(ModelType type) {
        if (ObjUtil.isNull(type)) {
            throw new AIModelException("Model type is null");
        }
        return processors.computeIfAbsent(type, t -> switch (t) {
            case OPENAI -> new OpenAIProcessor(config);
            case GEMINI -> new GeminiProcessor(config);
        });
    }
}
