package org.samagrata.npbackend.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.samagrata.npbackend.dto.ResourceRequest;
import org.samagrata.npbackend.dto.ResourceResponse;
import org.samagrata.npbackend.dto.SubjectDto;
import org.samagrata.npbackend.entity.CaseEntity;
import org.samagrata.npbackend.entity.ResourceEntity;
import org.samagrata.npbackend.entity.SubjectEntity;
import org.samagrata.npbackend.exception.ResourceNotFoundException;
import org.samagrata.npbackend.repository.CaseRepository;
import org.samagrata.npbackend.repository.ResourceRepository;
import org.samagrata.npbackend.repository.SubjectRepository;
import org.samagrata.npbackend.service.ResourceService;
import org.samagrata.npbackend.utils.DateConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ResourceSvcImpl implements ResourceService {
  
  @Autowired
  private ResourceRepository resourceRepo;

  @Autowired
  private CaseRepository caseRepo;

  @Autowired
  private SubjectRepository subjectRepo;

  @Override
  @Transactional(readOnly = true)
  public List<ResourceResponse> getAll() {
    List<ResourceResponse> resourceResponses = new ArrayList<>();
    List<ResourceEntity> resourceEntities = this.resourceRepo.findAll();

    for (ResourceEntity resourceEntity : resourceEntities) {
      ResourceResponse resourceResponse = buildResourceResponse(
        resourceEntity, new CaseEntity()
      );

      SubjectEntity subjectEntity = 
        subjectRepo.findbyAssociation(
          resourceEntity.getClass().getSimpleName(),
          resourceEntity.getId()
        ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          resourceEntity.getClass().getSimpleName() +
          " and association ID:" + resourceEntity.getId()
        )
      );
      
      if (subjectEntity != null) {
        resourceResponse.setSubject(buildSubjecDto(subjectEntity));
      }
      
      resourceResponses.add(resourceResponse);
    }
    
    return resourceResponses;
  }

  @Override
  @Transactional(readOnly = true)
  public ResourceResponse get(Long id) {
    ResourceEntity resourceEntity = this.resourceRepo.findById(id)
                                        .orElseThrow(() -> 
                                          new ResourceNotFoundException(
                                            "Resource not found with ID: " + id
                                          )
                                        );
    
    ResourceResponse resourceResponse = buildResourceResponse(
      resourceEntity, new CaseEntity()
    );

    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        resourceEntity.getClass().getSimpleName(),
        resourceEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          resourceEntity.getClass().getSimpleName() +
          " and association ID:" + resourceEntity.getId()
        )
      );
    resourceResponse.setSubject(buildSubjecDto(subjectEntity));

    return resourceResponse;
  }

  @Override
  public ResourceResponse create(ResourceRequest resourceRequest) {
    ResourceEntity resourceEntity = new ResourceEntity();
    CaseEntity caseEntity = new CaseEntity();

    if (!resourceRequest.caseNumber().isBlank()) {
      caseEntity = caseRepo.findByCaseNumber(resourceRequest.caseNumber())
                            .orElseThrow(() -> 
                              new ResourceNotFoundException(
                                "Case not found with case number: " + 
                                resourceRequest.caseNumber()
                              )
                            );

      resourceEntity.setCaseId(caseEntity.getId());
    }

    resourceEntity.setType(resourceRequest.type());
        
    resourceEntity.setEngagedUntil(
      DateConversion.convertStrToLDT(resourceRequest.engagedUntil())
    );

    try {
      resourceEntity.setHours(Float.parseFloat(resourceRequest.hours()));
    } catch (NumberFormatException e) {
      log.error("Resource hours value was invalid", e);
    }
    
    resourceEntity.setRemark(resourceRequest.remark());

    SubjectEntity subjectEntity = new SubjectEntity();
    subjectEntity.setName(resourceRequest.subject().getName());
    subjectEntity.setEmail(resourceRequest.subject().getEmail());
    subjectEntity.setPhone(resourceRequest.subject().getContactNumber());
    subjectEntity.setAddress(resourceRequest.subject().getAddress());
    subjectEntity.setCity(resourceRequest.subject().getCity());
    subjectEntity.setState(resourceRequest.subject().getState());
    subjectEntity.setZip(resourceRequest.subject().getZip());

    ResourceEntity savedResource = this.resourceRepo.save(resourceEntity);
    
    subjectEntity.setAssociatedType(resourceEntity.getClass().getSimpleName());
    subjectEntity.setAssociatedId(savedResource.getId());
    SubjectEntity savedSubject = this.subjectRepo.save(subjectEntity);

    ResourceResponse resourceResponse = buildResourceResponse(
      savedResource, caseEntity
    );
    resourceResponse.setSubject(buildSubjecDto(savedSubject));

    return resourceResponse;
  }

  @Override
  public ResourceResponse update(Long id, ResourceRequest resourceRequest) {
    ResourceEntity resourceEntity = this.resourceRepo.findById(id)
                                         .orElseThrow(() -> 
                                            new ResourceNotFoundException(
                                              "Resource not found with ID: " + id
                                            )
                                          );
    
    CaseEntity caseEntity = new CaseEntity();
    if (!resourceRequest.caseNumber().isBlank()) {
      caseEntity = caseRepo.findByCaseNumber(resourceRequest.caseNumber())
                            .orElseThrow(() -> 
                              new ResourceNotFoundException(
                                "Case not found with case number: " + 
                                resourceRequest.caseNumber()
                              )
                            );

      resourceEntity.setCaseId(caseEntity.getId());
    }

    resourceEntity.setType(resourceRequest.type());

    resourceEntity.setEngagedUntil(
      DateConversion.convertStrToLDT(resourceRequest.engagedUntil())
    );

    try {
      resourceEntity.setHours(Float.parseFloat(resourceRequest.hours()));
    } catch (NumberFormatException e) {
      log.error("Resource hours value was invalid", e);
    }
    
    resourceEntity.setRemark(resourceRequest.remark());

    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        resourceEntity.getClass().getSimpleName(),
        resourceEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          resourceEntity.getClass().getSimpleName() +
          " and association ID:" + resourceEntity.getId()
        )
      );
    subjectEntity.setName(resourceRequest.subject().getName());
    subjectEntity.setEmail(resourceRequest.subject().getEmail());
    subjectEntity.setPhone(resourceRequest.subject().getContactNumber());
    subjectEntity.setAddress(resourceRequest.subject().getAddress());
    subjectEntity.setCity(resourceRequest.subject().getCity());
    subjectEntity.setState(resourceRequest.subject().getState());
    subjectEntity.setZip(resourceRequest.subject().getZip());

    ResourceEntity savedResource = this.resourceRepo.save(resourceEntity);
    SubjectEntity savedSubject = this.subjectRepo.save(subjectEntity);

    ResourceResponse resourceResponse = buildResourceResponse(
      savedResource, caseEntity
    );
    resourceResponse.setSubject(buildSubjecDto(savedSubject));

    return resourceResponse;
  }

  @Override
  public ResourceResponse patch(Long id, ResourceRequest resourceRequest) {
    ResourceEntity resourceEntity = this.resourceRepo.findById(id)
                                        .orElseThrow(() -> 
                                          new ResourceNotFoundException(
                                            "Resource not found with ID: " + id
                                          )
                                        );
    
    CaseEntity caseEntity = new CaseEntity();
    if(!resourceRequest.caseNumber().isBlank()) {
      caseEntity = caseRepo.findByCaseNumber(resourceRequest.caseNumber())
                            .orElseThrow(() -> 
                              new ResourceNotFoundException(
                                "Case not found with case number: " + 
                                resourceRequest.caseNumber()
                              )
                            );
      resourceEntity.setCaseId(caseEntity.getId());
    }

    if (!resourceRequest.type().isBlank()) {
      resourceEntity.setType(resourceRequest.type());
    }

    resourceEntity.setEngagedUntil(
      DateConversion.convertStrToLDT(resourceRequest.engagedUntil())
    );

    if (!resourceRequest.hours().isBlank()) {
      try {
        resourceEntity.setHours(Float.parseFloat(resourceRequest.hours()));
      } catch (NumberFormatException e) {
        log.error("Resource hours value was invalid", e);
      }
    }
    
    if (!resourceRequest.remark().isBlank()) {
      resourceEntity.setRemark(resourceRequest.remark());
    }

    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        resourceEntity.getClass().getSimpleName(),
        resourceEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          resourceEntity.getClass().getSimpleName() +
          " and association ID:" + resourceEntity.getId()
        )
      );
    if(!resourceRequest.subject().getName().isBlank()) {
      subjectEntity.setName(resourceRequest.subject().getName());
    }
    
    if(!resourceRequest.subject().getEmail().isBlank()) {
      subjectEntity.setEmail(resourceRequest.subject().getEmail());
    }
    
    if(!resourceRequest.subject().getContactNumber().isBlank()) {
      subjectEntity.setPhone(resourceRequest.subject().getContactNumber());
    }

    if(!resourceRequest.subject().getAddress().isBlank()) {
      subjectEntity.setAddress(resourceRequest.subject().getAddress());
    }

    if(!resourceRequest.subject().getCity().isBlank()) {
      subjectEntity.setCity(resourceRequest.subject().getCity());
    }

    if(!resourceRequest.subject().getState().isBlank()) {
      subjectEntity.setState(resourceRequest.subject().getState());
    }
    
    if(!resourceRequest.subject().getZip().isBlank()) {
      subjectEntity.setZip(resourceRequest.subject().getZip());
    }

    ResourceEntity savedResource = this.resourceRepo.save(resourceEntity);
    SubjectEntity savedSubject = this.subjectRepo.save(subjectEntity);

    ResourceResponse resourceResponse = buildResourceResponse(
      savedResource, caseEntity
    );
    resourceResponse.setSubject(buildSubjecDto(savedSubject));

    return resourceResponse;
  }

  @Override
  public void delete(Long id) {
    ResourceEntity resourceEntity = this.resourceRepo.findById(id)
                                        .orElseThrow(() -> 
                                          new ResourceNotFoundException(
                                            "Resource not found with ID: " + id
                                          )
                                        );
    
    SubjectEntity subjectEntity = 
      subjectRepo.findbyAssociation(
        resourceEntity.getClass().getSimpleName(),
        resourceEntity.getId()
      ).orElseThrow(() -> 
        new ResourceNotFoundException(
          "Subject not found with association type: " + 
          resourceEntity.getClass().getSimpleName() +
          " and association ID:" + resourceEntity.getId()
        )
      );
    
    subjectRepo.delete(subjectEntity);
    resourceRepo.delete(resourceEntity);
  }

  private ResourceResponse buildResourceResponse(
    ResourceEntity resourceEntity,
    CaseEntity caseEntity
  ) {
    ResourceResponse resourceResponse = new ResourceResponse();

    resourceResponse.setId(resourceEntity.getId());

    if (
      caseEntity.getId() == null && 
      resourceEntity.getCaseId() != null
    ) {
      CaseEntity ce = caseRepo.findById(resourceEntity.getCaseId())
                              .orElseThrow(() -> 
                                new ResourceNotFoundException(
                                  "Case not found with id: " + 
                                  resourceEntity.getCaseId()
                                )
                              );
      resourceResponse.setCaseNumber(ce.getCaseNumber());
    } else {
      resourceResponse.setCaseNumber(caseEntity.getCaseNumber());
    }
    
    resourceResponse.setType(resourceEntity.getType());

    if (resourceEntity.getCaseId() != null) {
      resourceResponse.setEngagedSince(
        resourceEntity.getCreatedAt().format(
          DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
      );
    }
    
    if (resourceEntity.getEngagedUntil() != null) {
      resourceResponse.setEngagedUntil(
        resourceEntity.getEngagedUntil().format(
          DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
      );
    }

    if (resourceEntity.getHours() != null) {
      resourceResponse.setHours(String.valueOf(
        resourceEntity.getHours()
      ));
    }

    resourceResponse.setRemark(resourceEntity.getRemark());

    return resourceResponse;
  }

  private SubjectDto buildSubjecDto(SubjectEntity subject) {
    SubjectDto resourceSubject = new SubjectDto();
    resourceSubject.setId(subject.getId());
    resourceSubject.setName(subject.getName());
    resourceSubject.setEmail(subject.getEmail());
    resourceSubject.setContactNumber(subject.getPhone());
    resourceSubject.setAddress(subject.getAddress());
    resourceSubject.setCity(subject.getCity());
    resourceSubject.setState(subject.getState());
    resourceSubject.setZip(subject.getZip());

    return resourceSubject;
  }
}
