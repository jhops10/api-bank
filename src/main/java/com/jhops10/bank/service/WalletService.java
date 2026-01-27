package com.jhops10.bank.service;

import com.jhops10.bank.controller.dto.*;
import com.jhops10.bank.entities.Deposit;
import com.jhops10.bank.entities.Wallet;
import com.jhops10.bank.exception.DeleteWalletException;
import com.jhops10.bank.exception.StatementException;
import com.jhops10.bank.exception.WalletDataAlreadyExistException;
import com.jhops10.bank.exception.WalletNotFoundException;
import com.jhops10.bank.repository.DepositRepository;
import com.jhops10.bank.repository.WalletRepository;
import com.jhops10.bank.repository.dto.StatementView;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public StatementDto getStatements(UUID walletId, Integer page, Integer pageSize) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("There is no wallet with this id, walletId:" + walletId));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statements = walletRepository.findStatements(walletId.toString(), pageRequest)
                .map(view -> mapToDto(walletId, view));

        return new StatementDto(
                new WalletDto(wallet.getWalletId(), wallet.getCpf(), wallet.getName(), wallet.getEmail(), wallet.getBalance()),
                statements.getContent(),
                new PaginationDto(statements.getNumber(), statements.getSize(),statements.getTotalElements(), statements.getTotalPages())
        );



    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {
        if (view.getType().equalsIgnoreCase("deposit")) {
            return mapToDeposit(view);
        }

        if (view.getType().equalsIgnoreCase("transfer") && view.getWalletSender().equalsIgnoreCase(walletId.toString())) {
            return mapToStatementItemWhenSender(walletId, view);
        }

        if (view.getType().equalsIgnoreCase("transfer") && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())) {
            return mapToStatementItemWhenReceiver(walletId, view);
        }

        throw new StatementException("Invalid type " + view.getType());
    }

    private StatementItemDto mapToStatementItemWhenReceiver(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money received from" + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementItemDto mapToStatementItemWhenSender(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money send to" + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT
        );
    }

    private static StatementItemDto mapToDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }
}
