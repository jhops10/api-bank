package com.jhops10.bank.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankApiException.class)
    public ProblemDetail handleBankApiException(BankApiException ex) {
        return ex.toProblemDetail();
    }
}
