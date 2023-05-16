package com.expense_tracker.repository;

import com.expense_tracker.model.db.ExpenseParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipantEntity, Integer> {
}
