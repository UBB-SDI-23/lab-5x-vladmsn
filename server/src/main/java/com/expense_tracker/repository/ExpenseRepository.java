package com.expense_tracker.repository;

import com.expense_tracker.model.db.ExpenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    Page<ExpenseEntity> findAllByPayerId(Integer payerId, Pageable pageable);

    Page<ExpenseEntity> findAllByGroupId(Integer groupId, Pageable pageable);
}
