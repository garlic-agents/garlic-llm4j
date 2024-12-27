package com.garlic.agents.llm.providers.gemini;

import com.garlic.agents.llm.core.ModelConfig;
import com.garlic.agents.llm.core.ModelProcessor;
/**
 * gemini processor
 *
 * @author MoChenYa
 * @date 2024/12/27
 * @since 1.0
 */
public class GeminiProcessor implements ModelProcessor {

    private final ModelConfig config;

    public GeminiProcessor(ModelConfig config) {
        this.config = config;
    }
}
