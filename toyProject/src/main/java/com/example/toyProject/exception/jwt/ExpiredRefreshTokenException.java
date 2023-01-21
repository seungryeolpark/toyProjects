package com.example.toyProject.exception.jwt;

import com.example.toyProject.dto.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ExpiredRefreshTokenException extends RuntimeException {

    private ErrorCode errorCode;
    public ExpiredRefreshTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
