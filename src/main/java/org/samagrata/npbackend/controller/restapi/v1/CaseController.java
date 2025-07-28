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

import org.samagrata.npbackend.dto.CaseRequest;
import org.samagrata.npbackend.dto.CaseResponse;
import org.samagrata.npbackend.service.CaseService;

@RestController
@RequestMapping("/api/v1/cases")
public record CaseController(CaseService caseSvc) {

  @GetMapping
  public ResponseEntity<List<CaseResponse>> getAll() {
    return ResponseEntity.ok(caseSvc.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CaseResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(caseSvc.get(id));
  }

  @PostMapping
  public ResponseEntity<CaseResponse> create(
    @Valid @RequestBody CaseRequest caseRequest
  ) {
    CaseResponse caseResponse = caseSvc.create(caseRequest);
    return ResponseEntity.ok().body(caseResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CaseResponse> update(
    @PathVariable Long id, 
    @Valid @RequestBody CaseRequest caseRequest
  ) {
    CaseResponse caseResponse = this.caseSvc.update(id, caseRequest);
    return ResponseEntity.ok(caseResponse);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CaseResponse> patch(
    @PathVariable Long id, 
    @Valid @RequestBody CaseRequest caseRequest
  ) {
    CaseResponse caseResponse = this.caseSvc.patch(id, caseRequest);
    return ResponseEntity.ok(caseResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    caseSvc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
