package org.samagrata.npbackend.controller.restapi.v1;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.samagrata.npbackend.dto.AuthRequest;
import org.samagrata.npbackend.dto.AuthResponse;
import org.samagrata.npbackend.entity.UserEntity;
import org.samagrata.npbackend.repository.UserRepository;
import org.samagrata.npbackend.security.JwtUtil;
import org.samagrata.npbackend.utils.ValidationUtil;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthController(
    AuthenticationManager authenticationManager,
    JwtUtil jwtUtil,
    UserRepository userRepository,
    PasswordEncoder passwordEncoder
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(
    @RequestBody AuthRequest authRequest
  ) {

    Authentication authentication = null;
    Optional<UserEntity> userEntity = userRepository.findByUsername(authRequest.username());
    if (userEntity.isPresent()) {
      String password = userEntity.get().getPassword();
      if (passwordEncoder.matches(password, authRequest.password())) {
        authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
            authRequest.username(),
            authRequest.password()
          )
        );
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("{\"message\": \"Wrong credentials\"}");
      }
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body("{\"message\": \"Username not present\"}");
    }
    
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthResponse(jwt));
  }

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(
    @RequestBody AuthRequest authRequest
  ) throws JsonProcessingException {
    
    if (
      userRepository.findByUsername(
        authRequest.username()
       ).isPresent()
    ) {
      return new ResponseEntity<>(
        "{\"message\": \"Username is already taken!\"}",
        HttpStatus.BAD_REQUEST
      );
    }

    Map<String, String> passwordValidation = 
      ValidationUtil.validatePassword(authRequest.password());

    if (!passwordValidation.isEmpty()) {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonMgs = objectMapper.writeValueAsString(passwordValidation);
      return new ResponseEntity<>(jsonMgs, HttpStatus.BAD_REQUEST);
    }
    
    UserEntity user = new UserEntity();
    user.setUsername(authRequest.username());
    user.setPassword(
      passwordEncoder.encode(authRequest.password())
    );
    user.setRole("ROLE_ADMIN"); // Default role

    userRepository.save(user);

    return new ResponseEntity<>(
      "{\"message\": \"User registered successfully!\"}",
      HttpStatus.OK
    );
  }
}
