package com.jhops10.bank.service;

import com.jhops10.bank.controller.dto.CreateWalletDto;
import com.jhops10.bank.entities.Wallet;
import com.jhops10.bank.exception.WalletDataAlreadyExistException;
import com.jhops10.bank.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());

        if (walletDb.isPresent()) {
           throw new WalletDataAlreadyExistException("CPF or Email Already Exists");
        }

        var wallet = new Wallet();

        wallet.setBalance(BigDecimal.ZERO);
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());

        return walletRepository.save(wallet);
    }
}
