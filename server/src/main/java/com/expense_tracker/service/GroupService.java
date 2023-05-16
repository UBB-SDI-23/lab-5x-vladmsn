package com.expense_tracker.service;

import com.expense_tracker.exceptions.JpaEntityNotFoundException;
import com.expense_tracker.model.Expense;
import com.expense_tracker.model.Group;
import com.expense_tracker.model.User;
import com.expense_tracker.model.db.ExpenseParticipantEntity;
import com.expense_tracker.model.db.GroupEntity;
import com.expense_tracker.model.db.GroupUserEntity;
import com.expense_tracker.model.group.GroupWithBalance;
import com.expense_tracker.model.util.GroupConverter;
import com.expense_tracker.repository.GroupRepository;
import com.expense_tracker.repository.GroupUserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class GroupService {
    private final UserService userService;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final ExpenseService expenseService;

    @Transactional(readOnly = true)
    public List<Group> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupConverter::convertToGroup)
                .peek(group -> group.setParticipants(getParticipantsByGroupId(group.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public Group getGroupById(Integer id) {
        Group group = GroupConverter.convertToGroup(groupRepository.findById(id)
                .orElseThrow(() -> new JpaEntityNotFoundException("Group with id: " + id + " not found")));
        group.setParticipants(getParticipantsByGroupId(id));

        return group;
    }

    @Transactional(readOnly = true)
    public List<GroupWithBalance> getGroupsByUserId(Integer userId) {
        return groupUserRepository.findAll().stream()
                .filter(groupUserEntity -> groupUserEntity.getPk().getUserId().equals(userId))
                .map(groupUserEntity -> groupRepository.findById(groupUserEntity.getPk().getGroupId())
                    .orElseThrow(() -> new JpaEntityNotFoundException("Group with id: " + groupUserEntity.getPk().getGroupId() + " not found")))
                .map(groupEntity -> {
                    Double totalSpent = expenseService.getByPayerIdAndGroupId(userId, groupEntity.getId()).stream()
                            .mapToDouble(Expense::getAmount)
                            .sum();

                    Double totalOwed = expenseService.getParticipantIdAndGroupId(userId, groupEntity.getId()).stream()
                            .mapToDouble(ExpenseParticipantEntity::getAmountOwed)
                            .sum();

                    GroupUserEntity.GroupUsersPk groupUsersPk = new GroupUserEntity.GroupUsersPk();
                    groupUsersPk.setGroupId(groupEntity.getId());
                    groupUsersPk.setUserId(userId);

                    LocalDate joinedAt = groupUserRepository.findById(groupUsersPk)
                            .orElseThrow(() -> new JpaEntityNotFoundException("GroupUser with id: " + userId + " and " + groupEntity.getId() + " not found"))
                            .getJoinedAt();

                    return GroupWithBalance.builder()
                            .id(groupEntity.getId())
                            .name(groupEntity.getName())
                            .currency(groupEntity.getPreferredCurrency())
                            .description(groupEntity.getDescription())
                            .joinedAt(joinedAt)
                            .balance(totalSpent-totalOwed)
                            .build();
                })
                .toList();
    }

    @Transactional
    public GroupEntity createGroup(GroupEntity group) {
        if (group.getId() != null) {
            throw new IllegalArgumentException("Group id must be null");
        }

        return groupRepository.save(group);
    }

    @Transactional
    public GroupEntity updateGroup(GroupEntity group) {
        if (group.getId() == null) {
            throw new IllegalArgumentException("Group id must not be null");
        }
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Integer id) {
        groupRepository.deleteById(id);
    }

    private Set<User> getParticipantsByGroupId(Integer id) {
        return StreamSupport.stream(groupUserRepository.findAll().spliterator(), false)
                .filter(groupUserEntity -> groupUserEntity.getPk().getGroupId().equals(id))
                .map(groupUserEntity -> userService.getById(groupUserEntity.getPk().getUserId()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void addParticipant(Integer groupId, Integer userId) {
        GroupUserEntity.GroupUsersPk groupUsersPk = new GroupUserEntity.GroupUsersPk();
        groupUsersPk.setGroupId(groupId);
        groupUsersPk.setUserId(userId);

        GroupUserEntity groupUserEntity = new GroupUserEntity();
        groupUserEntity.setPk(groupUsersPk);
        groupUserEntity.setJoinedAt(LocalDate.now());
        groupUserEntity.setIsActive(true);
        groupUserEntity.setNickname(userService.getById(userId).getUsername());

        groupUserRepository.save(groupUserEntity);
    }

    @Transactional
    public void removeParticipant(Integer groupId, Integer userId) {
        GroupUserEntity.GroupUsersPk groupUsersPk = new GroupUserEntity.GroupUsersPk();
        groupUsersPk.setGroupId(groupId);
        groupUsersPk.setUserId(userId);

        groupUserRepository.deleteById(groupUsersPk);
    }
}
