package com.example.toyProject.exception.notEqual;

import com.example.toyProject.dto.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotEqualPasswordException extends RuntimeException {

    private ErrorCode errorCode;
    public NotEqualPasswordException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
