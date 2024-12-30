package com.garlic.agents.llm.enums;

/**
 * model type
 *
 * @author MoChenYa
 * @since 1.0
 */
public enum ModelType {

    GEMINI("gemini"),

    OPENAI("openai"),
    ;

    private final String name;

    ModelType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ModelType fromName(String name) {
        for (ModelType type : ModelType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
