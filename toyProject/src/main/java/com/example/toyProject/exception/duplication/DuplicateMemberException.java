package com.example.toyProject.exception.duplication;

import com.example.toyProject.dto.enums.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateMemberException extends RuntimeException {

    private ErrorCode errorCode;
    public DuplicateMemberException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
