package com.swipecrafts.school.data.model.api;

/**
 * Created by Madhusudan Sapkota on 2/26/2018.
 */

public enum UserType {

    PARENT("parent"),
    TEACHER("teacher"),
    DRIVER("driver"),
    GENERAL("general");

    private final String value;

    UserType(String type) {
        value = type;
    }

    public String getValue() {
        return value;
    }

    public static UserType from(String from) {

        switch (from) {
            case "parent":
                return PARENT;
            case "teacher":
                return TEACHER;
            case "driver":
                return DRIVER;
            default:
                return PARENT;
        }
    }
}
