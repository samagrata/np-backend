package org.samagrata.npbackend.repository;

import java.util.ArrayList;

import org.samagrata.npbackend.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
  @Query("SELECT c FROM CommentEntity AS c WHERE c.associatedType = ?1 AND c.associatedId = ?2")
	ArrayList<CommentEntity> findbyAssociation(String aType, Long aId);
}
