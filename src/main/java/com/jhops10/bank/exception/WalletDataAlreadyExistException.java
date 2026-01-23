package com.jhops10.bank.exception;

import org.springframework.http.ProblemDetail;

public class WalletDataAlreadyExistException extends BankApiException {


    private final String detail;

    public WalletDataAlreadyExistException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("Wallet data already exists");
        pd.setDetail(detail);

        return pd;
    }
}
