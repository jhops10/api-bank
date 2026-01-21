package com.jhops10.bank.exception;

public abstract class BankApiException extends RuntimeException {

    public BankApiException(String message) {
        super(message);
    }

    public BankApiException(Throwable cause) {
        super(cause);
    }
}
