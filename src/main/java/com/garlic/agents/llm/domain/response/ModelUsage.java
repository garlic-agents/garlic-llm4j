package com.garlic.agents.llm.domain.response;


import java.io.Serial;
import java.io.Serializable;

/**
 * ModelUsage
 *
 * @author MoChenYa
 * @since 1.0
 */
public class ModelUsage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long promptTokens;

    private Long completionTokens;

    private Long totalTokens;

    public ModelUsage() {
    }

    public ModelUsage(Long promptTokens, Long completionTokens, Long totalTokens) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
    }

    public Long getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Long promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Long getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Long completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Long getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Long totalTokens) {
        this.totalTokens = totalTokens;
    }

    public static final class Builder {
        private Long promptTokens;
        private Long completionTokens;
        private Long totalTokens;

        public Builder() {
        }

        public Builder promptTokens(Long promptTokens) {
            this.promptTokens = promptTokens;
            return this;
        }

        public Builder completionTokens(Long completionTokens) {
            this.completionTokens = completionTokens;
            return this;
        }

        public Builder totalTokens(Long totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public ModelUsage build() {
            ModelUsage modelUsage = new ModelUsage();
            modelUsage.setPromptTokens(promptTokens);
            modelUsage.setCompletionTokens(completionTokens);
            modelUsage.setTotalTokens(totalTokens);
            return modelUsage;
        }
    }
}
