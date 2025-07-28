package org.samagrata.npbackend.controller.restapi.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.samagrata.npbackend.dto.StoryRequest;
import org.samagrata.npbackend.dto.StoryResponse;
import org.samagrata.npbackend.entity.CommentEntity;
import org.samagrata.npbackend.service.StoryService;

@RestController
@RequestMapping("/api/v1/stories")
public record StoryController(StoryService storySvc) {

  @GetMapping
  public ResponseEntity<List<StoryResponse>> getAll() {
    return ResponseEntity.ok(storySvc.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<StoryResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(storySvc.get(id));
  }

  @PostMapping
  public ResponseEntity<StoryResponse> create(
    @Valid @RequestBody StoryRequest resourceRequest
  ) {
    StoryResponse resourceResponse = 
      storySvc.create(resourceRequest);
    return ResponseEntity.ok().body(resourceResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<StoryResponse> update(
    @PathVariable Long id, 
    @Valid @RequestBody StoryRequest resourceRequest
  ) {
    StoryResponse resourceResponse = 
      this.storySvc.update(id, resourceRequest);
    return ResponseEntity.ok(resourceResponse);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<StoryResponse> patch(
    @PathVariable Long id, 
    @Valid @RequestBody StoryRequest resourceRequest
  ) {
    StoryResponse resourceResponse = 
      this.storySvc.patch(id, resourceRequest);
    return ResponseEntity.ok(resourceResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    storySvc.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{storyId}/comments")
  public ResponseEntity<List<CommentEntity>> getAllComments(
    @PathVariable Long storyId
  ) {
    return ResponseEntity.ok(storySvc.getAllComments(storyId));
  }

  @PostMapping("/{storyId}/comments")
  public ResponseEntity<CommentEntity> createComment(
    @PathVariable Long storyId, 
    @Valid @RequestBody CommentEntity commentRequest
  ) {
    CommentEntity commentResponse = 
      storySvc.createComment(storyId, commentRequest);
    return ResponseEntity.ok().body(commentResponse);
  }

  @PatchMapping("/{storyId}/comments/{id}")
  public ResponseEntity<CommentEntity> patchComment(
    @PathVariable Long id, 
    @Valid @RequestBody CommentEntity commentRequest
  ) {
    CommentEntity commentResponse = 
      this.storySvc.patchComment(id, commentRequest);
    return ResponseEntity.ok(commentResponse);
  }
}
