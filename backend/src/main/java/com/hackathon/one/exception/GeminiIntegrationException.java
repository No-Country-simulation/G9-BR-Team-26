package com.hackathon.one.exception;

public class GeminiIntegrationException extends RuntimeException {
    public GeminiIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeminiIntegrationException(String message) {
        super(message);
    }
}
