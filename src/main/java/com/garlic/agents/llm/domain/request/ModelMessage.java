package com.garlic.agents.llm.domain.request;

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

    public static ModelMessage user(List<MessageContent> content) {
        return new ModelMessage(MessageRole.USER, content);
    }

    public static ModelMessage assistant(List<MessageContent> content) {
        return new ModelMessage(MessageRole.ASSISTANT, content);
    }

    public static ModelMessage system(List<MessageContent> content) {
        return new ModelMessage(MessageRole.SYSTEM, content);
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
