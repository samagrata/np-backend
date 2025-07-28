package org.samagrata.npbackend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.samagrata.npbackend.utils.MapListConverter;
import org.samagrata.npbackend.utils.StringListConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name = "stories")
public class StoryEntity implements BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
	private Long id;

  @Column(name = "case_id", unique = true, nullable = false)
	private Long caseId;

  @Column(nullable = false)
	private String title;

  @Convert(converter = StringListConverter.class)
  @Column(columnDefinition = "TEXT")
	private List<String> paras;

  @Convert(converter = StringListConverter.class)
  @Column(name = "carousel_images")
  private List<String> carouselImages;

  @Convert(converter = MapListConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<Map<String, String>> carousels;
  
  @Column(name = "publish_date")
	private LocalDate publishDate;

  private Boolean publish;

  @Lob
  @Column(name = "ss_file")
	private byte[] ssFile;

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
