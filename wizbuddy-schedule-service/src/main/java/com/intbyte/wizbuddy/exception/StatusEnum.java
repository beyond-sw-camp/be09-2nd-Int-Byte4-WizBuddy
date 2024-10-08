package com.intbyte.wizbuddy.exception;

import lombok.Getter;

@Getter
public enum StatusEnum {

    BOARD_NOT_FOUND(404, "BOARD_NOT_FOUND"),
    SCHEDULE_NOT_FOUND(404, "SCHEDULE_NOT_FOUND"),
    EMPLOYEE_CODE_NOT_FOUND(404, "EMPLOYEE_CODE_NOT_FOUND"),
    SCHEDULE_CODE_DUPLICATE(403, "SCHEDULE_CODE_DUPLICATE"),
    COMMENT_NOT_FOUND(404, "COMMENT_NOT_FOUND"),
    WORKINGPART_NOT_EQUALS(409, "WORKINGPART_NOT_EQUALS"),
    ALREADY_ADOPTED(403, "ALREADY_ADOPTED");

    private final int statusCode;
    private final String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}
