package com.garlic.agents.llm.domain;

import com.garlic.agents.llm.domain.enums.MessageContentType;

import java.io.Serial;
import java.io.Serializable;

/**
 * MessageContent
 *
 * @author MoChenYa
 * @since 1.0
 */
public class MessageContent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private MessageContentType type;

    private String text;

    private String image;

    public MessageContent() {
    }

    public MessageContent(MessageContentType type, String text, String image) {
        this.type = type;
        this.text = text;
        this.image = image;
    }

    public MessageContentType getType() {
        return type;
    }

    public void setType(MessageContentType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
