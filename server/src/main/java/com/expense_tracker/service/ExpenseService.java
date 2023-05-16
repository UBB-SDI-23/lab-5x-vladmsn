package com.expense_tracker.service;

import com.expense_tracker.exceptions.JpaEntityNotFoundException;
import com.expense_tracker.model.Expense;
import com.expense_tracker.model.db.ExpenseEntity;
import com.expense_tracker.model.db.ExpenseParticipantEntity;
import com.expense_tracker.model.util.ExpenseConverter;
import com.expense_tracker.repository.ExpenseParticipantRepository;
import com.expense_tracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final ExpenseParticipantRepository expenseParticipantRepository;

    @Transactional(readOnly = true)
    public List<Expense> getAll() {
        return StreamSupport
                .stream(expenseRepository.findAll().spliterator(), false)
                .map(ExpenseConverter::convertToExpense)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExpenseEntity getById(Integer id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new JpaEntityNotFoundException("Expense with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<ExpenseParticipantEntity> getParticipantsByExpenseId(Integer expenseId) {
        return StreamSupport
                .stream(expenseParticipantRepository.findAll().spliterator(), false)
                .filter(expenseParticipantEntity -> expenseParticipantEntity.getPk().getExpenseId().equals(expenseId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Expense> getByUserId(Integer userId) {
        return StreamSupport.stream(
                expenseRepository.findAll().spliterator(), false)
                .filter(transactionEntity -> transactionEntity.getPayerId().equals(userId))
                .map(ExpenseConverter::convertToExpense)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Expense> getByGroupId(Integer groupId, Date date) {
        return expenseRepository.findAll().stream()
                .filter(transactionEntity -> transactionEntity.getGroupId().equals(groupId))
                .filter(transactionEntity -> {
                    if (date == null) {
                        return true;
                    }

                    return transactionEntity.getTransactionDate().after(date);
                })
                .sorted(Comparator.comparing(ExpenseEntity::getTransactionDate))
                .map(expense -> {
                    Expense expense1 = ExpenseConverter.convertToExpense(expense);
                    expense1.setPayer(userService.getById(expense.getPayerId()));
                    return expense1;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseParticipantEntity> getParticipantIdAndGroupId(Integer id, Integer groupId) {
        return StreamSupport.stream(expenseParticipantRepository.findAll().spliterator(), false)
                .filter(expenseParticipantEntity -> expenseParticipantEntity.getPk().getUserId().equals(id))
                .filter(expenseParticipantEntity -> expenseParticipantEntity.getPk().getExpenseId().equals(groupId))
                .collect(Collectors.toList());
    }

    public List<ExpenseEntity> getExpensesByParticipant(Integer participantId) {
        return StreamSupport.stream(expenseParticipantRepository.findAll().spliterator(), false)
                .filter(expenseParticipantEntity -> expenseParticipantEntity.getPk().getUserId().equals(participantId))
                .map(expenseParticipantEntity -> expenseRepository.findById(expenseParticipantEntity.getPk().getExpenseId()).orElse(null))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Expense> getByPayerId(Integer payerId) {
        return StreamSupport.stream(
                expenseRepository.findAll().spliterator(), false)
                .filter(transactionEntity -> transactionEntity.getPayerId().equals(payerId))
                .map(ExpenseConverter::convertToExpense)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Expense> getByPayerIdAndGroupId(Integer payerId, Integer groupId) {
        return StreamSupport.stream(
                expenseRepository.findAll().spliterator(), false)
                .filter(transactionEntity -> transactionEntity.getPayerId().equals(payerId))
                .filter(transactionEntity -> transactionEntity.getGroupId().equals(groupId))
                .map(ExpenseConverter::convertToExpense)
                .collect(Collectors.toList());
    }

    @Transactional
    public Expense create(ExpenseEntity expenseEntity) {
        if (expenseEntity.getId() != null) {
            throw new IllegalArgumentException("Expense id must be null");
        }

        try {
            return ExpenseConverter.convertToExpense(expenseRepository.save(expenseEntity));
        }
        catch (DataIntegrityViolationException e) {
            log.error("Jpa exception when creating new expense, user id may be invalid {}", expenseEntity.getPayerId(), e);
            throw new JpaEntityNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public Expense update(ExpenseEntity expenseEntity) {
        if (expenseEntity.getId() == null) {
            throw new IllegalArgumentException("Expense id must not be null");
        }

        try {
            return ExpenseConverter.convertToExpense(expenseRepository.save(expenseEntity));
        }
        catch (DataIntegrityViolationException e) {
            log.error("Jpa exception when creating new expense, user id may be invalid {}", expenseEntity.getPayerId(), e);
            throw new JpaEntityNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public void delete(Integer id) {
        expenseRepository.deleteById(id);
    }

    @Transactional
    public ExpenseParticipantEntity addParticipant(ExpenseParticipantEntity expenseParticipantEntity) {
        try {
            return expenseParticipantRepository.save(expenseParticipantEntity);
        }
        catch (DataIntegrityViolationException e) {
            log.error("Jpa exception when creating new expense participant, user id may be invalid {}", expenseParticipantEntity.getPk().getUserId(), e);
            throw new JpaEntityNotFoundException(e.getMessage());
        }
    }
}
