package org.samagrata.npbackend.service;

import java.util.List;

import org.samagrata.npbackend.dto.ResourceRequest;
import org.samagrata.npbackend.dto.ResourceResponse;
import org.samagrata.npbackend.exception.ResourceNotFoundException;

public interface ResourceService {
  List<ResourceResponse> getAll() throws ResourceNotFoundException;
  ResourceResponse get(Long id) throws ResourceNotFoundException;
  ResourceResponse create(ResourceRequest entity) throws ResourceNotFoundException;
  ResourceResponse update(Long id, ResourceRequest entity) throws ResourceNotFoundException;
  ResourceResponse patch(Long id, ResourceRequest entity) throws ResourceNotFoundException;
  void delete(Long id) throws ResourceNotFoundException;
}
