package com.jhops10.bank.service;

import com.jhops10.bank.controller.dto.CreateWalletDto;
import com.jhops10.bank.controller.dto.DepositMoneyDto;
import com.jhops10.bank.entities.Deposit;
import com.jhops10.bank.entities.Wallet;
import com.jhops10.bank.exception.DeleteWalletException;
import com.jhops10.bank.exception.WalletDataAlreadyExistException;
import com.jhops10.bank.exception.WalletNotFoundException;
import com.jhops10.bank.repository.DepositRepository;
import com.jhops10.bank.repository.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
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

    @Transactional
    public void depositMoney(UUID walletId, @Valid DepositMoneyDto dto, String ipAddress) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("There is no wallet with this id"));

        var deposit = new Deposit();
        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.depositValue());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.depositValue()));
        walletRepository.save(wallet);

    }
}
