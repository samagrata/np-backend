CREATE TABLE files (
	id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  associated_type  VARCHAR(255),
  associated_id    BIGINT,
	file_name        VARCHAR(255),
	file_type        VARCHAR(125),
  file_blob        MEDIUMBLOB,
  created_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_files_associated_type ON files (associated_type);
CREATE INDEX idx_files_associated_id ON files (associated_id);
