package com.example.toyProject.exception;

public class NotEqualPassword extends RuntimeException {

    public NotEqualPassword() {
        super();
    }

    public NotEqualPassword(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEqualPassword(String message) {
        super(message);
    }

    public NotEqualPassword(Throwable cause) {
        super(cause);
    }
}
