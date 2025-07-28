CREATE TABLE funds (
	id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  associated_type  VARCHAR(255),
  associated_id    BIGINT,
	goal_amount      FLOAT,
  created_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_funds_associated_type ON funds (associated_type);
CREATE INDEX idx_funds_associated_id ON funds (associated_id);
