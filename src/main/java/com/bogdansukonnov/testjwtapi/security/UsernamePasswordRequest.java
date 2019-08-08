package com.bogdansukonnov.testjwtapi.security;

import javax.validation.constraints.NotEmpty;

public class UsernamePasswordRequest extends UsernameRequest {
    @NotEmpty(message = "password must not be empty")
    private String password;

    public UsernamePasswordRequest() {
    }

    public UsernamePasswordRequest(String username, String password) {
        super(username);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
