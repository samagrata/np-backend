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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "cases")
public class CaseEntity implements BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
	private Long id;

  @Pattern(
    regexp = "^[A-Z]{2}[0-9]{3}$",
    message = "must have 2 letters in uppercase, followed by 3 digits"
  )
  @Column(name = "case_number", unique = true, nullable = false)
	private String caseNumber;
  
  @Column(name = "closing_date")
	private LocalDateTime closingDate;
	
  private String file;

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
