package com.expense_tracker.service;

import com.expense_tracker.model.Expense;
import com.expense_tracker.model.User;
import com.expense_tracker.model.db.ExpenseEntity;
import com.expense_tracker.model.db.ExpenseParticipantEntity;
import com.expense_tracker.model.expense.ExpenseDetails;
import com.expense_tracker.model.group.GroupExpenseSummary;
import com.expense_tracker.model.user.UserExpenses;
import com.expense_tracker.model.user.UserGroupDetails;
import com.expense_tracker.model.util.ExpenseConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseTrackerService {
    private final ExpenseService expenseService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public UserExpenses getUserExpensesByGroup(Integer userId, Integer groupId, Pageable pageable) {
        User user = userService.getById(userId);
        List<Expense> expenses = expenseService.getByPayerId(userId, pageable)
                .stream()
                .filter(expenseEntity -> {
                    if (groupId == null) {
                        return true;
                    }

                    return expenseEntity.getGroupId().equals(groupId);
                })
                .collect(Collectors.toList());

        return UserExpenses.builder()
                .id(user.getId())
                .username(user.getUsername())
                .preferredCurrency(user.getPreferredCurrency())
                .expenses(expenses)
                .build();
    }

    @Transactional(readOnly = true)
    public ExpenseDetails getExpenseDetails(Integer expenseId) {
        ExpenseEntity expenseEntity = expenseService.getById(expenseId);
        User payer = userService.getById(expenseEntity.getPayerId());
        HashMap<User, Double> participants_amount = expenseService.getParticipantsByExpenseId(expenseId)
                .stream()
                .collect(HashMap::new,
                        (map, expenseParticipantEntity) -> {
                            map.put(userService.getById(expenseParticipantEntity.getPk().getUserId()), expenseParticipantEntity.getAmountOwed());
                        },
                        HashMap::putAll);

        return ExpenseConverter.convertToExpenseDetails(expenseEntity, payer, participants_amount);
    }

    // #TODO: FIX THIS
    @Transactional(readOnly = true)
    public List<UserGroupDetails> getUserGroupDetails(Integer group_id, Pageable pageable) {
        List<User> users = userService.getAll(Pageable.unpaged()).getContent();

        return users.stream()
                .map(user -> {
                    Double totalSpent = expenseService.getByPayerIdAndGroupId(user.getId(), group_id).stream()
                                            .mapToDouble(Expense::getAmount)
                                            .sum();

                    Double totalOwed = expenseService.getParticipantIdAndGroupId(user.getId(), group_id).stream()
                                        .mapToDouble(ExpenseParticipantEntity::getAmountOwed)
                                        .sum();

                    return UserGroupDetails.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .preferredCurrency(user.getPreferredCurrency())
                            .balance(totalSpent - totalOwed)
                            .totalPayed(totalSpent)
                            .totalShare(totalOwed)
                            .build();
                })
                .sorted((t1, t2) -> t2.getBalance().compareTo(t1.getBalance()))
                .collect(Collectors.toList());
    }

    public ExpenseDetails addParticipantToExpense(Integer id, Integer participantId, Double amount) {
        ExpenseEntity expenseEntity = expenseService.getById(id);
        User participant = userService.getById(participantId);

        if (expenseEntity.getAmount() < amount) {
            throw new IllegalArgumentException("Amount owed cannot be greater than the expense amount");
        }

        ExpenseParticipantEntity expenseParticipantEntity = getExpenseParticipantEntity(id, participantId, amount);

        expenseService.addParticipant(expenseParticipantEntity);

        return getExpenseDetails(id);
    }

    private static ExpenseParticipantEntity getExpenseParticipantEntity(Integer id, Integer participantId, Double amount) {
        ExpenseParticipantEntity expenseParticipantEntity = new ExpenseParticipantEntity();
        ExpenseParticipantEntity.ExpenseUserPK pk = new ExpenseParticipantEntity.ExpenseUserPK();
        pk.setExpenseId(id);
        pk.setUserId(participantId);
        expenseParticipantEntity.setPk(pk);
        expenseParticipantEntity.setAmountOwed(amount);
        return expenseParticipantEntity;
    }

    public GroupExpenseSummary getTotalExpenseSummary(Integer groupId) {

        Double totalSpent = expenseService.getByGroupId(groupId, null, Pageable.unpaged()).stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        return GroupExpenseSummary.builder()
                .groupId(groupId)
                .totalSpent(totalSpent)
                .timestamp(new java.util.Date())
                .build();
    }
}
