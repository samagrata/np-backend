package org.samagrata.npbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.samagrata.npbackend.entity.SubjectEntity;
import org.samagrata.npbackend.exception.ResourceNotFoundException;
import org.samagrata.npbackend.repository.SubjectRepository;
import org.samagrata.npbackend.service.SubjectService;

@Service
@Transactional
public class SubjectSvcImpl implements SubjectService {
  
  @Autowired
  private SubjectRepository subjectRepo;

  @Override
  @Transactional(readOnly = true)
  public List<SubjectEntity> getAll() {
    return this.subjectRepo.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<SubjectEntity> get(Long id) {
     return this.subjectRepo.findById(id);
  }

  @Override
  public SubjectEntity create(SubjectEntity entity) {
    return this.subjectRepo.save(entity);
  }

  @Override
  public SubjectEntity update(Long id, SubjectEntity entity) {
    SubjectEntity subject = this.subjectRepo.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));

    subject.setName(entity.getName());
    subject.setPhone(entity.getPhone());
    subject.setEmail(entity.getEmail());
    subject.setAddress(entity.getAddress());
    subject.setCity(entity.getCity());
    subject.setState(entity.getState());
    subject.setZip(entity.getZip());

    return subjectRepo.save(subject);
  }

  @Override
  public void delete(Long id) {
    SubjectEntity subject = this.subjectRepo.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
    subjectRepo.delete(subject);
  }
}
