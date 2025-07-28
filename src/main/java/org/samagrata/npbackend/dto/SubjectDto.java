package org.samagrata.npbackend.dto;

import lombok.Data;

@Data
public class SubjectDto {
  private Long id;
  private String name = "";
  private String contactNumber = "";
  private String email = "";
  private String address = "";
  private String city = "";
  private String state = "";
  private String zip = "";
}