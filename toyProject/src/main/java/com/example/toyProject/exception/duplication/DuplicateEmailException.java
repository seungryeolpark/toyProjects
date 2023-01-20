package com.example.toyProject.exception.duplication;

import com.example.toyProject.dto.enums.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException {

    private ErrorCode errorCode;
    public DuplicateEmailException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
