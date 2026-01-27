package com.jhops10.bank.exception;

import org.springframework.http.ProblemDetail;

public class TransferException extends BankApiException {

    private final String detail;


    public TransferException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("Transfer not allowed");
        pd.setDetail(detail);

        return pd;
    }
}
