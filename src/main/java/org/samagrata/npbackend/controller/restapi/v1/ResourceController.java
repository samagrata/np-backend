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

import org.samagrata.npbackend.dto.ResourceRequest;
import org.samagrata.npbackend.dto.ResourceResponse;
import org.samagrata.npbackend.service.ResourceService;

@RestController
@RequestMapping("/api/v1/resources")
public record ResourceController(ResourceService resourceSvc) {

  @GetMapping
  public ResponseEntity<List<ResourceResponse>> getAll() {
    return ResponseEntity.ok(resourceSvc.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResourceResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(resourceSvc.get(id));
  }

  @PostMapping
  public ResponseEntity<ResourceResponse> create(
    @Valid @RequestBody ResourceRequest resourceRequest
  ) {
    ResourceResponse resourceResponse = resourceSvc.create(resourceRequest);
    return ResponseEntity.ok().body(resourceResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResourceResponse> update(
    @PathVariable Long id, 
    @Valid @RequestBody ResourceRequest resourceRequest
  ) {
    ResourceResponse resourceResponse = this.resourceSvc.update(id, resourceRequest);
    return ResponseEntity.ok(resourceResponse);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ResourceResponse> patch(
    @PathVariable Long id, 
    @Valid @RequestBody ResourceRequest resourceRequest
  ) {
    ResourceResponse resourceResponse = this.resourceSvc.patch(id, resourceRequest);
    return ResponseEntity.ok(resourceResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    resourceSvc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
