package org.samagrata.npbackend.controller.restapi.v1;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.samagrata.npbackend.entity.SubjectEntity;
import org.samagrata.npbackend.service.SubjectService;

@RestController
@RequestMapping("/api/v1/subjects")
public record SubjectController(SubjectService subjectSvc) {

  @GetMapping
  public ResponseEntity<List<SubjectEntity>> getAll() {
      return ResponseEntity.ok(subjectSvc.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SubjectEntity> get(@PathVariable Long id) {
    Optional<SubjectEntity> subject = subjectSvc.get(id);
    return subject.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<SubjectEntity> create(
    @Valid @RequestBody SubjectEntity entity
  ) {
    SubjectEntity createdSubjectEntity = subjectSvc.create(entity);
    return ResponseEntity.ok().body(createdSubjectEntity);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SubjectEntity> update(
    @PathVariable Long id, 
    @Valid @RequestBody SubjectEntity entity
  ) {
    SubjectEntity updatedEntity = this.subjectSvc.update(id, entity);
    return ResponseEntity.ok(updatedEntity);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    subjectSvc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
