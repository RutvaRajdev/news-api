package com.fhf.it.news.api.model;

import lombok.Value;

@Value
public class ErrorResponseWrapper {
    private String errorMessage;
    private int errorCode;

    public ErrorResponseWrapper(String message, int code) {
        this.errorMessage = message;
        this.errorCode = code;
    }
}
