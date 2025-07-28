package org.samagrata.npbackend.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.samagrata.npbackend.utils.StringListConverter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "volunteers", indexes = {
  @Index(name = "idx_volunteers_full_name", columnList = "full_name")
})
public class VolunteerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
	private Long id;

  @Size(
    min = 2,
    max = 100,
    message = "should be at least 2 chars"
  )
  @Column(name = "full_name", nullable = false)
	private String fullName;
  
  @Column(unique = true, nullable = false)
  @Email(message = "is not valid")
  private String email;

  private String phone;

  @Column(name = "age_range")
	private String ageRange;

  @Convert(converter = StringListConverter.class)
	private List<String> interests;

	private String availability;

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
