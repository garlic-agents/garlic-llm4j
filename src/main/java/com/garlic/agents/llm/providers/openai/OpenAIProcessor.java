package com.garlic.agents.llm.providers.openai;

import com.garlic.agents.llm.core.ModelConfig;
import com.garlic.agents.llm.core.ModelProcessor;

/**
 * openai processor
 *
 * @author MoChenYa
 * @date 2024/12/27
 * @since 1.0
 */
public class OpenAIProcessor implements ModelProcessor {

    private final ModelConfig config;

    public OpenAIProcessor(ModelConfig config) {
        this.config = config;
    }
}
