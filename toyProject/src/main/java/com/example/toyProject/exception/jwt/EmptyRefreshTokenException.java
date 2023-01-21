package com.example.toyProject.exception.jwt;

import com.example.toyProject.dto.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EmptyRefreshTokenException extends RuntimeException {

    private ErrorCode errorCode;
    public EmptyRefreshTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
