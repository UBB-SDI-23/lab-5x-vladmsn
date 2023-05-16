package com.expense_tracker.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "expense_participant")
public class ExpenseParticipantEntity {
    @EmbeddedId
    private ExpenseUserPK pk;
    @Column(name = "amount_owed")
    private Double amountOwed;

    @Embeddable
    @Data
    public static class ExpenseUserPK implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @Column(name = "user_id")
        private Integer userId;
        @Column(name = "expense_id")
        private Integer expenseId;
    }
}
