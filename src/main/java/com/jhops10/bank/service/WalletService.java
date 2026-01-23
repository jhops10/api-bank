package com.jhops10.bank.service;

import com.jhops10.bank.controller.dto.CreateWalletDto;
import com.jhops10.bank.entities.Wallet;
import com.jhops10.bank.exception.DeleteWalletException;
import com.jhops10.bank.exception.WalletDataAlreadyExistException;
import com.jhops10.bank.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());

        if (walletDb.isPresent()) {
           throw new WalletDataAlreadyExistException("CPF or email already exists");
        }

        var wallet = new Wallet();

        wallet.setBalance(BigDecimal.ZERO);
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());

        return walletRepository.save(wallet);
    }

    public Boolean deleteWallet(UUID walletId) {

        var wallet = walletRepository.findById(walletId);

        if (wallet.isPresent()) {

            if (wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new DeleteWalletException("The wallet balance is not zero. The current amount is R$ " + wallet.get().getBalance());
            }

            walletRepository.delete(wallet.get());
        }

        return wallet.isPresent();
    }
}
