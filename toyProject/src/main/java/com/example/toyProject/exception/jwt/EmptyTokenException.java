package com.example.toyProject.exception.jwt;

import com.example.toyProject.dto.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EmptyTokenException extends RuntimeException {

    private ErrorCode errorCode;
    public EmptyTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
