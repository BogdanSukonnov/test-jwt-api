package com.bogdansukonnov.testjwtapi.security;

import javax.validation.constraints.NotEmpty;

public class UsernameRequest {
    @NotEmpty(message = "username must not be empty")
    private String username;

    public UsernameRequest() {
    }

    public UsernameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
