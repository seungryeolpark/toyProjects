package com.example.toyProject.exception.notEqual;

import com.example.toyProject.dto.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotEqualClientIpException extends RuntimeException {

    private ErrorCode errorCode;
    public NotEqualClientIpException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
