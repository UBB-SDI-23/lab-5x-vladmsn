package com.expense_tracker.model.group;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class GroupExpenseSummary {
    private Integer groupId;
    private Double totalSpent;
    private Date timestamp;
}
