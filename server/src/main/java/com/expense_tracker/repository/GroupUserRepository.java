package com.expense_tracker.repository;

import com.expense_tracker.model.db.GroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUserEntity, GroupUserEntity.GroupUsersPk> {
}
