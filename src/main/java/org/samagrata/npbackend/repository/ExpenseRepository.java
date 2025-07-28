package org.samagrata.npbackend.repository;

import java.util.ArrayList;

import org.samagrata.npbackend.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
  ArrayList<ExpenseEntity> getByFundId(Long fundId);
}
