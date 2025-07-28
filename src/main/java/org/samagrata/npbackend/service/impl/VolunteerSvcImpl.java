package org.samagrata.npbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.samagrata.npbackend.exception.ResourceNotFoundException;
import org.samagrata.npbackend.entity.VolunteerEntity;
import org.samagrata.npbackend.repository.VolunteerRepository;
import org.samagrata.npbackend.service.VolunteerService;

@Service
@Transactional
public class VolunteerSvcImpl implements VolunteerService {
  
  @Autowired
  private VolunteerRepository volunteerRepo;

  @Override
  @Transactional(readOnly = true)
  public List<VolunteerEntity> getAll() {
    return this.volunteerRepo.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<VolunteerEntity> get(Long id) {
     return this.volunteerRepo.findById(id);
  }

  @Override
  public VolunteerEntity create(VolunteerEntity entity) {
    return this.volunteerRepo.save(entity);
  }

  @Override
  public VolunteerEntity update(Long id, VolunteerEntity entity) {
    VolunteerEntity volunteer = this.volunteerRepo.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException(
        "Volunteer not found with ID: " + id));

    volunteer.setFullName(entity.getFullName());
    volunteer.setEmail(entity.getEmail());
    volunteer.setPhone(entity.getPhone());
    volunteer.setAgeRange(entity.getAgeRange());
    volunteer.setInterests(entity.getInterests());
    volunteer.setAvailability(entity.getAvailability());

    return volunteerRepo.save(volunteer);
  }

  @Override
  public void delete(Long id) {
    VolunteerEntity volunteer = this.volunteerRepo.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException(
        "Volunteer not found with ID: " + id));
    volunteerRepo.delete(volunteer);
  }
}
