package org.samagrata.npbackend.dto;

import lombok.Data;

@Data
public class CaseResponse {
  private Long id;
  private String caseNumber = "";
  private String openingDate = "";
  private String closingDate = "";
  private Integer noOfRs = 0;
  private Integer tHours = 0;
  private SubjectDto subject;
}
