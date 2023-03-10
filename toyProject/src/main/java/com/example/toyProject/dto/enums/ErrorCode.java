package com.example.toyProject.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATION_EMAIL(500, "DUP-001", "EMAIL DUPLICATION"),
    DUPLICATION_MEMBER(500, "DUP-002", "MEMBER DUPLICATION"),
    EMPTY_ACCESS_TOKEN(403, "EMP-001", "ACCESS TOKEN EMPTY"),
    EMPTY_REFRESH_TOKEN(403, "EMP-002", "REFRESH TOKEN EMPTY"),
    NOT_EQUAL_CERT_TOKEN(500, "NEQ-001", "CERT TOKEN NOT EQUAL"),
    NOT_EQUAL_PASSWORD(500, "NEQ-002", "PASSWORD NOT EQUAL"),
    NOT_EQUAL_CLIENT_IP(401, "NEQ-003", "CLIENT IP NOT EQUAL"),
    NOT_EQUAL_JWT_TOKEN(401, "NEQ-004", "JWT TOKEN NOT EQUAL"),
    NOT_FOUND_MEMBER(500, "NFD-001", "MEMBER NOT FOUND"),
    Malformed_JWT_TOKEN(401, "MFJ-001", "MALFORMED JWT TOKEN"),
    EXPIRED_JWT_TOKEN(401, "EPJ-001", "EXPIRED JWT TOKEN"),
    EXPIRED_REFRESH_TOKEN(401, "EPJ-002", "EXPIRED REFRESH_TOKEN"),
    UN_SUPPORT_JWT_TOKEN(401, "USJ-001", "UN SUPPORT JWT TOKEN"),
    IllegalArgument(401, "IAJ-001", "IllegalArgument JWT TOKEN"),
    AuthenticationException(403, "AUTH-001", "Auth Fail"),
    EXCEPTION(500, "EXC-001", "EXCEPTION");

    private int status;
    private String errorCode;
    private String message;
}
