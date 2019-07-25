package com.htmlfinder.utils;

public enum Attribute {

    ID("id", 1),
    TITLE("title", 2),
    CLASS("class", 3),
    HREF("href", 4),
    REL("rel", 5),
    ONCLICK("onClick", 6);

    private final String name;
    private final Integer priority;

    Attribute(String name, Integer priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public Integer getPriority() {
        return priority;
    }
}
