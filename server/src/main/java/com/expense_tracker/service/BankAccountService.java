package com.expense_tracker.service;

import com.expense_tracker.exceptions.JpaEntityNotFoundException;
import com.expense_tracker.model.BankAccount;
import com.expense_tracker.model.util.BankAccountConverter;
import com.expense_tracker.repository.BankAccountRepository;
import com.expense_tracker.model.db.BankAccountEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    @Transactional(readOnly = true)
    public List<BankAccount> getAll() {
        return StreamSupport
                .stream(bankAccountRepository.findAll().spliterator(), false)
                .map(BankAccountConverter::convertToBankAccount)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BankAccountEntity getById(Integer id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new JpaEntityNotFoundException("Bank account with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<BankAccountEntity> getByUserId(Integer userId) {
        return StreamSupport.stream(
                bankAccountRepository.findAll().spliterator(), false)
                .filter(bankAccountEntity -> bankAccountEntity.getUserId().equals(userId))
                .toList();
    }

    @Transactional
    public BankAccountEntity create(BankAccountEntity bankAccountEntity) {
        if (bankAccountEntity.getId() != null) {
            throw new IllegalArgumentException("Bank account id must be null");
        }

        try {
            return bankAccountRepository.save(bankAccountEntity);
        } catch (DataIntegrityViolationException e) {
            log.error("Jpa exception when creating new bank account, user id may be invalid {}", bankAccountEntity.getUserId(), e);
            throw new JpaEntityNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public BankAccountEntity update(BankAccountEntity bankAccountEntity) {
        if (bankAccountEntity.getId() == null) {
            throw new IllegalArgumentException("Bank account id must not be null");
        }

        try {
            return bankAccountRepository.save(bankAccountEntity);
        } catch (DataIntegrityViolationException e) {
            log.error("Jpa exception when updating bank account {}", bankAccountEntity, e);
            throw new JpaEntityNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public void delete(Integer id) {
        bankAccountRepository.deleteById(id);
    }
}
