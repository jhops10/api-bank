package com.jhops10.bank.exception;

import org.springframework.http.ProblemDetail;

public class DeleteWalletException extends BankApiException {

    private final String detail;

    public DeleteWalletException(String detail) {
        super(detail);
        this.detail = detail;
    }


    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("You cannot delete this wallet");
        pd.setDetail(detail);

        return pd;
    }
}
