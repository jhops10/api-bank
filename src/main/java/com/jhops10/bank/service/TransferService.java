package com.jhops10.bank.service;

import com.jhops10.bank.controller.dto.TransferMoneyDto;
import com.jhops10.bank.entities.Transfer;
import com.jhops10.bank.entities.Wallet;
import com.jhops10.bank.exception.TransferException;
import com.jhops10.bank.exception.WalletNotFoundException;
import com.jhops10.bank.repository.TransferRepository;
import com.jhops10.bank.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }


    @Transactional
    public void transferMoney(TransferMoneyDto dto) {

        var sender = walletRepository.findById(dto.sender())
                .orElseThrow(() -> new WalletNotFoundException("Sender wallet does not exist"));

        var receiver = walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("Receiver wallet does not exist"));

        if (sender.getBalance().compareTo(dto.transferValue()) == -1) {
            throw new TransferException("Insufficient balance, your current balance is: " + sender.getBalance());
        }

        updateWallets(dto, sender, receiver);
        persistTransfer(dto, receiver, sender);

    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.transferValue()));
        receiver.setBalance(receiver.getBalance().add(dto.transferValue()));

        walletRepository.save(sender);
        walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDto dto, Wallet receiver, Wallet sender) {
        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.transferValue());
        transfer.setTransferDateTime(LocalDateTime.now());

        transferRepository.save(transfer);
    }
}
