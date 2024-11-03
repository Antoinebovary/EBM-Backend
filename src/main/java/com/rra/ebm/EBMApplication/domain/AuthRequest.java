package com.rra.ebm.EBMApplication.domain;

public class AuthRequest {
    private int username;
    private String password;
    private String roles;
    private boolean isDefault;

    public AuthRequest() {
    }

    public AuthRequest(int username, String password, String roles, boolean isDefault) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.isDefault = isDefault;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
