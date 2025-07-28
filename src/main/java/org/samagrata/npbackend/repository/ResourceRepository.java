package org.samagrata.npbackend.repository;

import org.samagrata.npbackend.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
	// Custom query methods can be added here
}
