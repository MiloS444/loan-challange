package com.milos.loanchallange.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class LoanRequestException extends RuntimeException {

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
