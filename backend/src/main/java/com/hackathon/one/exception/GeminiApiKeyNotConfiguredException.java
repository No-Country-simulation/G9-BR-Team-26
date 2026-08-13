package com.hackathon.one.exception;

public class GeminiApiKeyNotConfiguredException extends RuntimeException {

    public GeminiApiKeyNotConfiguredException() {
        super("A variável de ambiente GEMINI_API_KEY não está configurada.");
    }
}
