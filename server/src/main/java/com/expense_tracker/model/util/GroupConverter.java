package com.expense_tracker.model.util;

import com.expense_tracker.model.Group;
import com.expense_tracker.model.User;
import com.expense_tracker.model.db.GroupEntity;
import com.expense_tracker.model.group.GroupWithBalance;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class GroupConverter {
    public static Group convertToGroup(GroupEntity groupEntity) {
        return Group.builder()
                .id(groupEntity.getId())
                .name(groupEntity.getName())
                .description(groupEntity.getDescription())
                .currency(groupEntity.getPreferredCurrency())
                .build();
    }

    public static GroupWithBalance convertToGroupWithBalance(GroupEntity groupEntity) {
        return GroupWithBalance.builder()
                .id(groupEntity.getId())
                .name(groupEntity.getName())
                .currency(groupEntity.getPreferredCurrency())
                .description(groupEntity.getDescription())
                .build();
    }


    public static GroupEntity convertToGroupEntity(Group group) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(group.getId());
        groupEntity.setName(group.getName());
        groupEntity.setDescription(group.getDescription());
        groupEntity.setPreferredCurrency(group.getCurrency());
        return groupEntity;
    }
}
