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
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "files", indexes = {
  @Index(name = "idx_files_associated_type", columnList = "associated_type"),
  @Index(name = "idx_files_associated_id", columnList = "associated_id")
})
public class FileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(name = "associated_type")
  private String associatedType;

  @Column(name = "associated_id")
  private Long associatedId;

  @Column(name = "file_name", nullable = false)
  private String name;

  @Column(name = "file_type", nullable = false)
  private String type;

  @Lob
  @Column(name = "file_blob", nullable = false)
  private byte[] fileBlob;

  @Transient
  private String b64EncodedFile;

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