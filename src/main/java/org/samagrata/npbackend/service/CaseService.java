package org.samagrata.npbackend.service;

import java.util.List;

import org.samagrata.npbackend.dto.CaseRequest;
import org.samagrata.npbackend.dto.CaseResponse;
import org.samagrata.npbackend.exception.ResourceNotFoundException;

public interface CaseService {
  List<CaseResponse> getAll() throws ResourceNotFoundException;
  CaseResponse get(Long id) throws ResourceNotFoundException;
  CaseResponse create(CaseRequest entity);
  CaseResponse update(Long id, CaseRequest entity) throws ResourceNotFoundException;
  CaseResponse patch(Long id, CaseRequest entity) throws ResourceNotFoundException;
  void delete(Long id) throws ResourceNotFoundException;
}
