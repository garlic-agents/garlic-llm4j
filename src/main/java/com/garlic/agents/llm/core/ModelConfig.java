package com.garlic.agents.llm.core;

import java.io.Serial;
import java.io.Serializable;

/**
 * ModelConfig
 *
 * @author MoChenYa
 * @since 1.0
 */
public class ModelConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;

    private String apiKey;

    public static ModelConfig builder() {
        return new ModelConfig();
    }

    public ModelConfig build() {
        return this;
    }

    public ModelConfig url(String url) {
        this.url = url;
        return this;
    }

    public ModelConfig apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
