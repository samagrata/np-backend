package org.samagrata.npbackend.dto;

public record AuthRequest(
  String username,
  String password
) {}
