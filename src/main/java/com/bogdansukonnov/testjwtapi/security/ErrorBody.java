package com.bogdansukonnov.testjwtapi.security;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.Map;

public class ErrorBody {
    private String timestamp;
    private int status;
    private String error;
    private Map<String, String> fields;

    public ErrorBody(HttpStatus httpStatus, Map<String, String> fields) {
        timestamp = LocalDateTime.now().toString();
        this.error = httpStatus.getReasonPhrase();
        this.fields = fields;
        this.status = httpStatus.value();
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

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
