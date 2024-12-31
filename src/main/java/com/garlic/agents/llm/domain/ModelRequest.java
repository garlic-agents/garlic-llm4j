package com.garlic.agents.llm.domain;

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

    private String model;

    private List<ModelMessage> messages;

    public ModelRequest() {
    }

    public ModelRequest(String model, List<ModelMessage> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ModelMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ModelMessage> messages) {
        this.messages = messages;
    }
}
