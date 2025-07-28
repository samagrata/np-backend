package org.samagrata.npbackend.repository;

import java.util.Optional;

import org.samagrata.npbackend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
  @Query("SELECT f FROM FileEntity AS f WHERE f.associatedType = ?1 AND f.associatedId = ?2")
	Optional<FileEntity> findByAssociation(String aType, Long aId);
}
