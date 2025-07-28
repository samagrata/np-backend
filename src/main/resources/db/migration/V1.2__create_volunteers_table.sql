CREATE TABLE volunteers (
	id            BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
	full_name     VARCHAR(255),
	email         VARCHAR(255)    UNIQUE,
	phone         VARCHAR(255),
  age_range     VARCHAR(255),
  interests     VARCHAR(512),
  availability  VARCHAR(255),
  created_at    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_volunteers_full_name ON volunteers (full_name);
