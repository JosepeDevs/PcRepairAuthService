package com.josepedevs.domain.entity.valueobjects;


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
    
    public String getRoleName(Role role) {
        return role.name();
    }
}
