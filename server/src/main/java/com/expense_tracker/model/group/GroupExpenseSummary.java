package com.expense_tracker.model.group;

import lombok.Data;

import java.util.Date;

@Data
public class GroupExpenseSummary {
    private Integer groupId;
    private Double totalSpent;
    private Date timestamp;

    GroupExpenseSummary(Integer groupId, Double totalSpent, Date timestamp) {
        this.groupId = groupId;
        this.totalSpent = totalSpent;
        this.timestamp = timestamp;
    }

    public static GroupExpenseSummaryBuilder builder() {
        return new GroupExpenseSummaryBuilder();
    }

    public static class GroupExpenseSummaryBuilder {
        private Integer groupId;
        private Double totalSpent;
        private Date timestamp;

        GroupExpenseSummaryBuilder() {
        }

        public GroupExpenseSummaryBuilder groupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public GroupExpenseSummaryBuilder totalSpent(Double totalSpent) {
            this.totalSpent = totalSpent;
            return this;
        }

        public GroupExpenseSummaryBuilder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public GroupExpenseSummary build() {
            return new GroupExpenseSummary(this.groupId, this.totalSpent, this.timestamp);
        }

        public String toString() {
            return "GroupExpenseSummary.GroupExpenseSummaryBuilder(groupId=" + this.groupId + ", totalSpent=" + this.totalSpent + ", timestamp=" + this.timestamp + ")";
        }
    }
}
