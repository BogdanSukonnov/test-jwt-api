package com.bogdansukonnov.testjwtapi.security;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class OkBody {
    private String timestamp;
    private final int status = 200;
    private String message;

    public OkBody(String message) {
        timestamp = LocalDateTime.now().toString();
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
