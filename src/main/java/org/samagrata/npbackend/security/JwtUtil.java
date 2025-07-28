package org.samagrata.npbackend.security;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import org.samagrata.npbackend.exception.JwtParsingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JwtUtil {

  private final JwtProperties jwtProperties;

  public JwtUtil(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  public String generateToken(UserDetails userDetails) {
    try {
      JWSSigner signer = new MACSigner(jwtProperties.getKey());

      JWTClaimsSet claimsSet = 
        new JWTClaimsSet.Builder()
              .subject(userDetails.getUsername())
              // .issuer(jwtProperties.getIssuer())
              .issueTime(new Date())
              .expirationTime(Date.from(
                  Instant.now().plusMillis(
                    jwtProperties.getExpiresIn()
                  )
                )
              )
              .claim("roles", 
                      userDetails.getAuthorities().stream()
                      .map(Object::toString)
                      .toList()
                    )
              .build();

      SignedJWT signedJWT = new SignedJWT(
        new JWSHeader(jwtProperties.getAlgorithm()),
        claimsSet
      );
      signedJWT.sign(signer);

      return signedJWT.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(
        "Error generating JWT token", e
      );
    }
  }

  public boolean validateToken(
    String token, 
    UserDetails userDetails
  ) {
    final String username = extractUsername(token);
    return (
      username.equals(userDetails.getUsername()) && 
      !isTokenExpired(token)
    );
  }

  public String extractUsername(String token) {
    return extractClaim(token, JWTClaimsSet::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, JWTClaimsSet::getExpirationTime);
  }

  public <T> T extractClaim(
    String token, 
    Function<JWTClaimsSet, T> claimsResolver
  ) {
    final JWTClaimsSet claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private JWTClaimsSet extractAllClaims(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(
        jwtProperties.getKey());

      if (!signedJWT.verify(verifier)) {
        throw new JOSEException(
          "JWT signature verification failed"
        );
      }
      return signedJWT.getJWTClaimsSet();
    } catch (ParseException | JOSEException e) {
      throw new JwtParsingException(
        "Error parsing or verifying JWT token"
      );
    }
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }
}
