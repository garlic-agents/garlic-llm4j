package com.garlic.agents.llm.core;

import com.garlic.agents.llm.domain.ModelRequest;
import com.garlic.agents.llm.domain.ModelResponse;

/**
 * ModelProcessor
 *
 * @author MoChenYa
 * @since 1.0
 */
public interface ModelProcessor {

    ModelResponse process(ModelRequest request);

}
