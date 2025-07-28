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

import org.samagrata.npbackend.entity.VolunteerEntity;
import org.samagrata.npbackend.service.VolunteerService;

@RestController
@RequestMapping("/api/v1/volunteers")
public record VolunteerController(VolunteerService volunteerSvc) {

  @GetMapping
  public ResponseEntity<List<VolunteerEntity>> getAll() {
    return ResponseEntity.ok(volunteerSvc.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<VolunteerEntity> get(@PathVariable Long id) {
    Optional<VolunteerEntity> volunteer = volunteerSvc.get(id);
    return volunteer.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<VolunteerEntity> create(
    @Valid @RequestBody VolunteerEntity entity
  ) {
    VolunteerEntity createdVolunteerEntity = volunteerSvc.create(entity);
    return ResponseEntity.ok().body(createdVolunteerEntity);
  }

  @PutMapping("/{id}")
  public ResponseEntity<VolunteerEntity> update(
    @PathVariable Long id, 
    @Valid @RequestBody VolunteerEntity entity
  ) {
    VolunteerEntity updatedEntity = this.volunteerSvc.update(id, entity);
    return ResponseEntity.ok(updatedEntity);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    volunteerSvc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
