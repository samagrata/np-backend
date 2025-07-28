package org.samagrata.npbackend.repository;

import java.util.Optional;

import org.samagrata.npbackend.entity.FundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends JpaRepository<FundEntity, Long> {
  @Query("SELECT f FROM FundEntity AS f WHERE f.associatedType = ?1 AND f.associatedId = ?2")
	Optional<FundEntity> findbyAssociation(String aType, Long aId);
}
