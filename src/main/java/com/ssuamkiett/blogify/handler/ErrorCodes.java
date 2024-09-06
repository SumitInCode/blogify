package com.ssuamkiett.blogify.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCodes {
    NO_CODE(0, "No Code", NOT_IMPLEMENTED),
    OPERATION_NOT_PERMITTED(1, "Operation not permitted", FORBIDDEN),
    ACCOUNT_LOCKED(302, "User account is locked", FORBIDDEN),
    INCORRECT_CURRENT_PASSWORD(300, "Current password is incorrect", BAD_REQUEST),
    ACCOUNT_DISABLED(303, "User account is disabled", FORBIDDEN),
    BAD_CREDENTIALS(304, "Username or Password not correct", FORBIDDEN),
    NEW_PASSWORD_DOES_NOT_MATCH(301, "The new password does not match", BAD_REQUEST),
    ENTITY_NOT_FOUND(404, "Entity not found", NOT_FOUND),
    METHOD_NOT_ALLOWED(405, "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}