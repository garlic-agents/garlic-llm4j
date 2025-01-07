package com.garlic.agents.llm.domain;

import com.garlic.agents.llm.domain.response.ModelUsage;
import com.garlic.agents.llm.enums.ResponseStatus;

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

    private ResponseStatus status;

    private String content;

    private ModelUsage usage;

    public ModelResponse() {
    }

    public ModelResponse(ResponseStatus status, String content) {
        this.status = status;
        this.content = content;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ModelUsage getUsage() {
        return usage;
    }

    public void setUsage(ModelUsage usage) {
        this.usage = usage;
    }


    public static final class Builder {
        private ResponseStatus status;
        private String content;
        private ModelUsage usage;

        public Builder() {
        }

        public Builder status(ResponseStatus status) {
            this.status = status;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder usage(ModelUsage usage) {
            this.usage = usage;
            return this;
        }

        public ModelResponse build() {
            ModelResponse modelResponse = new ModelResponse();
            modelResponse.setStatus(status);
            modelResponse.setContent(content);
            modelResponse.setUsage(usage);
            return modelResponse;
        }
    }
}
