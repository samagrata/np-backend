package org.samagrata.npbackend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
@Table(name = "receivables", indexes = {
  @Index(name = "idx_receivables_fund_id", columnList = "fund_id")
})
public class ReceivableEntity implements BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
	private Long id;

  @Column(name = "fund_id", nullable = false)
	private Long fundId;

  @Column(name = "reference_number", unique = true, nullable = false)
	private String referenceNumber;

  @Column(name = "receiving_date", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

  @Column(nullable = false)
	private Float amount;

	private String remark;
	
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
