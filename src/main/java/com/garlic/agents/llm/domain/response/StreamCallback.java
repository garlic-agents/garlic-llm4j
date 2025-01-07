package com.garlic.agents.llm.domain.response;

import com.garlic.agents.llm.domain.ModelResponse;

import java.util.function.Consumer;

/**
 * StreamCallback
 *
 * @author MoChenYa
 * @since 1.0
 */
public interface StreamCallback extends Consumer<ModelResponse> {
}
