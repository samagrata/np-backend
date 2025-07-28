package org.samagrata.npbackend.dto;

public record ResourceRequest (
  String type,
  String caseNumber,
  String engagedUntil,
  String hours,
  String remark,
  SubjectDto subject
) {}