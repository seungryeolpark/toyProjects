package com.example.toyProject.exception;

public class NotEqualCertTokenException extends RuntimeException {

    public NotEqualCertTokenException() {
        super();
    }

    public NotEqualCertTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEqualCertTokenException(String message) {
        super(message);
    }

    public NotEqualCertTokenException(Throwable cause) {
        super(cause);
    }
}
