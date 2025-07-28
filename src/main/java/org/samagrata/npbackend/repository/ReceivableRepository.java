package org.samagrata.npbackend.repository;

import java.util.ArrayList;

import org.samagrata.npbackend.entity.ReceivableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivableRepository extends JpaRepository<ReceivableEntity, Long> {
  ArrayList<ReceivableEntity> getByFundId(Long fundId);
}
