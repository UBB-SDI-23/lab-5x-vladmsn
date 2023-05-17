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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Expense> getAll(Pageable pageable) {
        return expenseRepository.findAll(pageable)
                .map(ExpenseConverter::convertToExpense);
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
    public Page<Expense> getByGroupId(Integer groupId, Date date, Pageable pageable) {
        return expenseRepository.findAllByGroupId(groupId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("transactionDate").descending()))
                .map(expense -> {
                    Expense expense1 = ExpenseConverter.convertToExpense(expense);
                    expense1.setPayer(userService.getById(expense.getPayerId()));
                    return expense1;
                });
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
    public Page<Expense> getByPayerId(Integer payerId, Pageable pageable) {
        return expenseRepository.findAllByPayerId(payerId, pageable)
                .map(ExpenseConverter::convertToExpense);
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
    public void addParticipant(ExpenseParticipantEntity expenseParticipantEntity) {
        try {
            expenseParticipantRepository.save(expenseParticipantEntity);
        }
        catch (DataIntegrityViolationException e) {
            log.error("Jpa exception when creating new expense participant, user id may be invalid {}", expenseParticipantEntity.getPk().getUserId(), e);
            throw new JpaEntityNotFoundException(e.getMessage());
        }
    }
}
