package com.jhops10.bank.exception;

import org.springframework.http.ProblemDetail;

public class WalletNotFoundException extends BankApiException {

    private final String detail;

    public WalletNotFoundException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(404);

        pd.setTitle("Wallet not found");
        pd.setDetail(detail);

        return pd;
    }
}
