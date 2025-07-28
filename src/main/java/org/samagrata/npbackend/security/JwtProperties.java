package org.samagrata.npbackend.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Validated
@Component
public class JwtProperties {
  
  private SecretKey key;

  private JWSAlgorithm algorithm;

  @NotNull
  @Value("${app.jwt.expires-in}")
  private long expiresIn;

  @NotEmpty
  @Value("${app.jwt.issuer}")
  private String issuer;

  public JwtProperties(
    @Value("${app.jwt.key}")
    String jwtKey,
    @Value("${app.jwt.algorithm}")
    String jwtAlgorithm
  ) {
    setKey(jwtKey);
    setAlgorithm(jwtAlgorithm);
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = JWSAlgorithm.parse(algorithm);
  }

  public void setKey(String jwtKey) {
    var jwk = new OctetSequenceKey.Builder(jwtKey.getBytes())
                                  .algorithm(algorithm)
                                  .build();
    
    this.key = jwk.toSecretKey();
  }
}
