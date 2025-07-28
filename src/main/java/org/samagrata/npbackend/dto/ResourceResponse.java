package org.samagrata.npbackend.dto;

import lombok.Data;

@Data
public class ResourceResponse {
  private Long id;
  private String type = "";
  private String caseNumber = "";
  private String engagedSince = "";
  private String engagedUntil = "";
  private String hours = "0";
  private String remark = "";
  private SubjectDto subject;
}
