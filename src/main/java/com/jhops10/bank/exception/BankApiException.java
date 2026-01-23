package com.jhops10.bank.exception;

import org.springframework.http.ProblemDetail;

public abstract class BankApiException extends RuntimeException {

    public BankApiException(String message) {
        super(message);
    }

    public BankApiException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(500);

        pd.setTitle("BankApi internal server error");
        pd.setDetail("Contact BankApi support");

        return pd;
    }
}
