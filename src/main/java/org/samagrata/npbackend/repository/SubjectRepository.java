package org.samagrata.npbackend.repository;

import java.util.Optional;

import org.samagrata.npbackend.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
  @Query("SELECT s FROM SubjectEntity AS s WHERE s.associatedType = ?1 AND s.associatedId = ?2")
	Optional<SubjectEntity> findbyAssociation(String aType, Long aId);
}
