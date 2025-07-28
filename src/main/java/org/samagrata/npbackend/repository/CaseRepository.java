package org.samagrata.npbackend.repository;

import java.util.Optional;

import org.samagrata.npbackend.entity.CaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<CaseEntity, Long> {
	Optional<CaseEntity> findByCaseNumber(String caseNumber);
}
