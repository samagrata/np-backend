package org.samagrata.npbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.samagrata.npbackend.entity.VolunteerEntity;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Long> {
	// Custom query methods can be added here
}
