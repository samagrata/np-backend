package org.samagrata.npbackend.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.samagrata.npbackend.dto.CaseRequest;
import org.samagrata.npbackend.dto.CaseResponse;
import org.samagrata.npbackend.dto.SubjectDto;
import org.samagrata.npbackend.entity.CaseEntity;
import org.samagrata.npbackend.entity.SubjectEntity;
import org.samagrata.npbackend.exception.ResourceNotFoundException;
import org.samagrata.npbackend.repository.CaseRepository;
import org.samagrata.npbackend.repository.SubjectRepository;
import org.samagrata.npbackend.service.CaseService;
import org.samagrata.npbackend.utils.DateConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CaseSvcImpl implements CaseService {
  
  @Autowired
  private CaseRepository caseRepo;

  @Autowired
  private SubjectRepository subjectRepo;

  @Override
  @Transactional(readOnly = true)
  public List<CaseResponse> getAll() {
    List<CaseResponse> caseResponses = new ArrayList<>();
    List<CaseEntity> caseEntities = this.caseRepo.findAll();

    for (CaseEntity caseEntity : caseEntities) {
      CaseResponse caseResponse = buildCaseResponse(caseEntity);

      SubjectEntity subjectEntity = 
        subjectRepo.findbyAssociation(
          caseEntity.getClass().getSimpleName(),
          caseEntity.getId()
        ).orElseThrow(() -> 
          new ResourceNotFoundException(
            "Subject not found with association type: " + 
            caseEntity.getClass().getSimpleName() +
            " and association ID:" + caseEntity.getId()
          )
        );
      
      if (subjectEntity != null) {
        caseResponse.setSubject(buildSubjectDto(subjectEntity));
      }
      
      caseResponses.add(caseResponse);
    }
    
    return caseResponses;
  }

  @Override
  @Transactional(readOnly = true)
  public CaseResponse get(Long id) {
    CaseEntity caseEntity = this.caseRepo.findById(id)
                                         .orElseThrow(() -> 
                                            new ResourceNotFoundException(
                                              "Case not found with ID: " + id
                                            )
                                          );
    
    CaseResponse caseResponse = buildCaseResponse(caseEntity);

    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        caseEntity.getClass().getSimpleName(),
        caseEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          caseEntity.getClass().getSimpleName() +
          " and association ID:" + caseEntity.getId()
        )
      );
    
    if (subjectEntity != null) {
      caseResponse.setSubject(buildSubjectDto(subjectEntity));
    }

    return caseResponse;
  }

  @Override
  public CaseResponse create(CaseRequest caseRequest) {
    CaseEntity caseEntity = new CaseEntity();

    caseEntity.setCaseNumber(caseRequest.caseNumber());
    caseEntity.setClosingDate(
      DateConversion.convertStrToLDT(caseRequest.closingDate())
    );

    SubjectEntity subjectEntity = new SubjectEntity();
    subjectEntity.setName(caseRequest.subject().getName());
    subjectEntity.setEmail(caseRequest.subject().getEmail());
    subjectEntity.setPhone(caseRequest.subject().getContactNumber());
    subjectEntity.setAddress(caseRequest.subject().getAddress());
    subjectEntity.setCity(caseRequest.subject().getCity());
    subjectEntity.setState(caseRequest.subject().getState());
    subjectEntity.setZip(caseRequest.subject().getZip());

    CaseEntity savedCase = this.caseRepo.save(caseEntity);
    
    subjectEntity.setAssociatedType(caseEntity.getClass().getSimpleName());
    subjectEntity.setAssociatedId(savedCase.getId());
    SubjectEntity savedSubject = this.subjectRepo.save(subjectEntity);

    CaseResponse caseResponse = buildCaseResponse(savedCase);
    caseResponse.setSubject(buildSubjectDto(savedSubject));

    return caseResponse;
  }

  @Override
  public CaseResponse update(Long id, CaseRequest caseRequest) {
    CaseEntity caseEntity = this.caseRepo.findById(id)
                                         .orElseThrow(() -> 
                                            new ResourceNotFoundException(
                                              "Case not found with ID: " + id
                                            )
                                          );

    caseEntity.setCaseNumber(caseRequest.caseNumber());
    caseEntity.setClosingDate(
      DateConversion.convertStrToLDT(caseRequest.closingDate())
    );

    SubjectEntity savedSubject = new SubjectEntity();
    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        caseEntity.getClass().getSimpleName(),
        caseEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          caseEntity.getClass().getSimpleName() +
          " and association ID:" + caseEntity.getId()
        )
      );
    if (subjectEntity != null) {
      subjectEntity.setName(caseRequest.subject().getName());
      subjectEntity.setEmail(caseRequest.subject().getEmail());
      subjectEntity.setPhone(caseRequest.subject().getContactNumber());
      subjectEntity.setAddress(caseRequest.subject().getAddress());
      subjectEntity.setCity(caseRequest.subject().getCity());
      subjectEntity.setState(caseRequest.subject().getState());
      subjectEntity.setZip(caseRequest.subject().getZip());
      savedSubject = this.subjectRepo.save(subjectEntity);
    }

    CaseEntity savedCase = this.caseRepo.save(caseEntity);

    CaseResponse caseResponse = buildCaseResponse(savedCase);
    caseResponse.setSubject(buildSubjectDto(savedSubject));

    return caseResponse;
  }

  @Override
  public CaseResponse patch(Long id, CaseRequest caseRequest) {
    CaseEntity caseEntity = this.caseRepo.findById(id)
                                         .orElseThrow(() -> 
                                            new ResourceNotFoundException(
                                              "Case not found with ID: " + id
                                            )
                                          );
    
    if(!caseRequest.caseNumber().isBlank()) {
      caseEntity.setCaseNumber(caseRequest.caseNumber());
    }
    
    caseEntity.setClosingDate(
      DateConversion.convertStrToLDT(caseRequest.closingDate())
    );

    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        caseEntity.getClass().getSimpleName(),
        caseEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          caseEntity.getClass().getSimpleName() +
          " and association ID:" + caseEntity.getId()
        )
      );
    if(!caseRequest.subject().getName().isBlank()) {
      subjectEntity.setName(caseRequest.subject().getName());
    }
    
    if(!caseRequest.subject().getEmail().isBlank()) {
      subjectEntity.setEmail(caseRequest.subject().getEmail());
    }
    
    if(!caseRequest.subject().getContactNumber().isBlank()) {
      subjectEntity.setPhone(caseRequest.subject().getContactNumber());
    }

    if(!caseRequest.subject().getAddress().isBlank()) {
      subjectEntity.setAddress(caseRequest.subject().getAddress());
    }

    if(!caseRequest.subject().getCity().isBlank()) {
      subjectEntity.setCity(caseRequest.subject().getCity());
    }

    if(!caseRequest.subject().getState().isBlank()) {
      subjectEntity.setState(caseRequest.subject().getState());
    }
    
    if(!caseRequest.subject().getZip().isBlank()) {
      subjectEntity.setZip(caseRequest.subject().getZip());
    }

    CaseEntity savedCase = this.caseRepo.save(caseEntity);
    SubjectEntity savedSubject = this.subjectRepo.save(subjectEntity);

    CaseResponse caseResponse = buildCaseResponse(savedCase);
    caseResponse.setSubject(buildSubjectDto(savedSubject));

    return caseResponse;
  }

  @Override
  public void delete(Long id) {
    CaseEntity caseEntity = this.caseRepo.findById(id)
                                         .orElseThrow(() -> 
                                            new ResourceNotFoundException(
                                              "Case not found with ID: " + id
                                            )
                                          );
    
    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        caseEntity.getClass().getSimpleName(),
        caseEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          caseEntity.getClass().getSimpleName() +
          " and association ID:" + caseEntity.getId()
        )
      );
    
    if (subjectEntity != null) {
      subjectRepo.delete(subjectEntity);
    }
    
    caseRepo.delete(caseEntity);
  }

  private CaseResponse buildCaseResponse(CaseEntity caseEntity) {
    CaseResponse caseResponse = new CaseResponse();

    caseResponse.setId(caseEntity.getId());
    caseResponse.setCaseNumber(caseEntity.getCaseNumber());
    caseResponse.setOpeningDate(
      caseEntity.getCreatedAt().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
      )
    );

    if (caseEntity.getClosingDate() != null) {
      caseResponse.setClosingDate(
        caseEntity.getClosingDate().format(
          DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
      );
    }

    caseResponse.setNoOfRs(0);
    caseResponse.setTHours(0);

    return caseResponse;
  }

  private SubjectDto buildSubjectDto(SubjectEntity subject) {
    SubjectDto caseSubject = new SubjectDto();
    caseSubject.setId(subject.getId());
    caseSubject.setName(subject.getName());
    caseSubject.setEmail(subject.getEmail());
    caseSubject.setContactNumber(subject.getPhone());
    caseSubject.setAddress(subject.getAddress());
    caseSubject.setCity(subject.getCity());
    caseSubject.setState(subject.getState());
    caseSubject.setZip(subject.getZip());

    return caseSubject;
  }
}
