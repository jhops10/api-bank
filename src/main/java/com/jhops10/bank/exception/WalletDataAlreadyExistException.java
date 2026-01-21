package com.jhops10.bank.exception;

public class WalletDataAlreadyExistException extends BankApiException {

    public WalletDataAlreadyExistException(String message) {
        super(message);
    }
}
