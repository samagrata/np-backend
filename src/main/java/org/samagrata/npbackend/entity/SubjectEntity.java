package org.samagrata.npbackend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subjects", indexes = {
  @Index(name = "idx_subjects_associated_type", columnList = "associated_type"),
  @Index(name = "idx_subjects_associated_id", columnList = "associated_id")
})
public class SubjectEntity implements BaseEntity {
	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
	private Long id;

  @Column(name = "associated_type")
  private String associatedType;

  @Column(name = "associated_id")
  private Long associatedId;

  // @Size(
  //   min = 2,
  //   max = 100,
  //   message = "should be at least 2 chars"
  // )
  @Column(nullable = false)
  private String name;

  @Column(unique = true)
  @Email(message = "is not valid")
	private String email;

  @Column(unique = true)
	private String phone;

	private String address;
	private String city;
	private String state;

  // @Pattern(
  //   regexp = "^[0-9]{6}$",
  //   message = "must be 6 digits"
  // )
  @Column(nullable = false)
	private String zip;

  @Setter(AccessLevel.NONE)
  @CreatedDate
  @CreationTimestamp(source = SourceType.DB)
  @Column(name = "created_at", nullable = false, updatable = false) 
  private LocalDateTime createdAt;

  @Setter(AccessLevel.NONE)
  @LastModifiedDate
  @UpdateTimestamp(source = SourceType.DB)
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
