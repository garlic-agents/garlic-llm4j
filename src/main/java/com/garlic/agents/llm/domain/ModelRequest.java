package com.garlic.agents.llm.domain;

import com.garlic.agents.llm.domain.request.ModelMessage;
import com.garlic.agents.llm.enums.ModelType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * ModelRequest
 *
 * @author MoChenYa
 * @since 1.0
 */
public class ModelRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String modelName;

    private ModelType modelType;

    private List<ModelMessage> messages;

    public ModelRequest() {
    }

    public ModelRequest(String modelName, ModelType modelType, List<ModelMessage> messages) {
        this.modelName = modelName;
        this.modelType = modelType;
        this.messages = messages;
    }

    public static ModelRequest chat(String modelName, List<ModelMessage> messages) {
        return new ModelRequest(modelName, ModelType.CHAT, messages);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<ModelMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ModelMessage> messages) {
        this.messages = messages;
    }
}
