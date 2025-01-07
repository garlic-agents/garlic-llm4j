package com.garlic.agents.llm.core;

import com.garlic.agents.llm.domain.ModelRequest;
import com.garlic.agents.llm.domain.ModelResponse;
import com.garlic.agents.llm.domain.response.StreamCallback;

/**
 * ModelProcessor
 *
 * @author MoChenYa
 * @since 1.0
 */
public interface ModelProcessor {

    ModelResponse process(ModelRequest request);

    void streamProcess(ModelRequest request, StreamCallback callback);
}
