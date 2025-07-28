package org.samagrata.npbackend.dto;

public record CaseRequest (
  String caseNumber,
  String closingDate,
  SubjectDto subject
) {}
