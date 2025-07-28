CREATE TABLE subjects (
	id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  associated_type  VARCHAR(255),
  associated_id    BIGINT,
	name             VARCHAR(255),
	email            VARCHAR(255)    UNIQUE,
	phone            VARCHAR(255),
	address          VARCHAR(255),
	city             VARCHAR(255),
	state            VARCHAR(255),
	zip              VARCHAR(255),
  created_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_subjects_associated_type ON subjects (associated_type);
CREATE INDEX idx_subjects_associated_id ON subjects (associated_id);
