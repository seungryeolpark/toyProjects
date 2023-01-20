package com.example.toyProject.exception;

import com.example.toyProject.dto.ErrorResponseDto;
import com.example.toyProject.exception.duplication.DuplicateEmailException;
import com.example.toyProject.exception.duplication.DuplicateMemberException;
import com.example.toyProject.exception.jwt.EmptyTokenException;
import com.example.toyProject.exception.notEqual.NotEqualCertTokenException;
import com.example.toyProject.exception.notEqual.NotEqualPasswordException;
import com.example.toyProject.exception.notEqual.NotFoundMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Exception ErrorCode 를 json 에 넣어서 ResponseEntity 응답을 하는 메소드
@RestControllerAdvice // 모든 restController 에서 발생하는 exception 을 처리한다.
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class) // 적힌 Exception 에 대해서 발생한 경우 처리하는 메소드
    public ResponseEntity<ErrorResponseDto> handleDuplicateEmailException(DuplicateEmailException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getErrorCode());
        return new ResponseEntity<>(responseDto,
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateMemberException(DuplicateMemberException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getErrorCode());
        return new ResponseEntity<>(responseDto,
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler(EmptyTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyTokenException(EmptyTokenException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getErrorCode());
        return new ResponseEntity<>(responseDto,
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler(NotEqualCertTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleNotEqualCertTokenException(NotEqualCertTokenException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getErrorCode());
        return new ResponseEntity<>(responseDto,
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler(NotEqualPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleNotEqualPasswordException(NotEqualPasswordException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getErrorCode());
        return new ResponseEntity<>(responseDto,
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundMemberException(NotFoundMemberException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getErrorCode());
        return new ResponseEntity<>(responseDto,
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }
}
