package com.garlic.agents.llm.providers.gemini;

import com.garlic.agents.llm.core.ModelConfig;
import com.garlic.agents.llm.core.ModelProcessor;
import com.garlic.agents.llm.domain.ModelRequest;
import com.garlic.agents.llm.domain.ModelResponse;

/**
 * gemini processor
 *
 * @author MoChenYa
 * @since 1.0
 */
public class GeminiProcessor implements ModelProcessor {

    private final ModelConfig config;

    public GeminiProcessor(ModelConfig config) {
        this.config = config;
    }

    @Override
    public ModelResponse process(ModelRequest request) {
        return null;
    }
}
