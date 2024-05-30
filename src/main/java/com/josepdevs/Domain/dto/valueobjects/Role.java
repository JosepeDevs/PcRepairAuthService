package com.josepdevs.Domain.dto.valueobjects;


public enum Role {
 	USER("Read"),
    EDITOR("Read, Write"),
    ADMIN("Read, Write, Delete");

    private final String permissions;

    Role(String permissions) {
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }
}
