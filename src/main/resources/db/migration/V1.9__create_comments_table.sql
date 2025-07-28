CREATE TABLE comments (
	id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  associated_type  VARCHAR(255),
  associated_id    BIGINT,
	name             VARCHAR(255),
	email            VARCHAR(255),
	text             VARCHAR(512),
	approved         BOOLEAN,
  created_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_comments_associated_type ON comments (associated_type);
CREATE INDEX idx_comments_associated_id ON comments (associated_id);
