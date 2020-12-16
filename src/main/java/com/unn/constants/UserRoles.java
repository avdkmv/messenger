package com.unn.constants;

public enum UserRoles {
    ADMIN("ADMIN"),
    USER("USER");

    private String value;

    UserRoles(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
