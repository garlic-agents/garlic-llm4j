package com.garlic.agents.llm.domain;

import com.garlic.agents.llm.domain.enums.ResponseType;

import java.io.Serial;
import java.io.Serializable;

/**
 * ModelResponse
 *
 * @author MoChenYa
 * @since 1.0
 */
public class ModelResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ResponseType type;

    private String content;

    public ModelResponse() {
    }

    public ModelResponse(ResponseType type, String content) {
        this.type = type;
        this.content = content;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
