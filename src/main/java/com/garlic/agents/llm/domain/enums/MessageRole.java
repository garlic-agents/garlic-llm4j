package com.garlic.agents.llm.domain.enums;

/**
 * MessageRole
 *
 * @author MoChenYa
 * @since 1.0
 */
public enum MessageRole {

    SYSTEM,
    ASSISTANT,
    USER,
    ;

    public String parseOpenaiRole() {
        return switch (this) {
            case SYSTEM -> "system";
            case ASSISTANT -> "assistant";
            case USER -> "user";
        };
    }
}
