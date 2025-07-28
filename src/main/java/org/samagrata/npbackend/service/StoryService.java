package org.samagrata.npbackend.service;

import java.util.List;

import org.samagrata.npbackend.dto.StoryRequest;
import org.samagrata.npbackend.dto.StoryResponse;
import org.samagrata.npbackend.entity.CommentEntity;
import org.samagrata.npbackend.exception.ResourceNotFoundException;

public interface StoryService {
  List<StoryResponse> getAll() throws ResourceNotFoundException;
  StoryResponse get(Long id) throws ResourceNotFoundException;
  StoryResponse create(StoryRequest entity) throws ResourceNotFoundException;
  StoryResponse update(Long id, StoryRequest entity) throws ResourceNotFoundException;
  StoryResponse patch(Long id, StoryRequest entity) throws ResourceNotFoundException;
  void delete(Long id) throws ResourceNotFoundException;

  List<CommentEntity> getAllComments(Long storyId) throws ResourceNotFoundException;
  CommentEntity createComment(Long storyId, CommentEntity entity) throws ResourceNotFoundException;
  CommentEntity patchComment(Long storyId, CommentEntity entity) throws ResourceNotFoundException;
}
