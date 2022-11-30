package com.example.devtoolslb5.tool;

public class GenerateCodeException extends RuntimeException {
    public GenerateCodeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public GenerateCodeException(String errorMessage) { super(errorMessage); }
}
