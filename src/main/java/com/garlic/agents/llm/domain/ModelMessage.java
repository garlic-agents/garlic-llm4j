package com.garlic.agents.llm.domain;

import com.garlic.agents.llm.domain.enums.MessageRole;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * ModelMessage
 *
 * @author MoChenYa
 * @since 1.0
 */
public class ModelMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private MessageRole role;

    private List<MessageContent> content;

    public ModelMessage() {
    }

    public ModelMessage(MessageRole role, List<MessageContent> content) {
        this.role = role;
        this.content = content;
    }

    public MessageRole getRole() {
        return role;
    }

    public void setRole(MessageRole role) {
        this.role = role;
    }

    public List<MessageContent> getContent() {
        return content;
    }

    public void setContent(List<MessageContent> content) {
        this.content = content;
    }
}
