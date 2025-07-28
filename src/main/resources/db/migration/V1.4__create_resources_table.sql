CREATE TABLE resources (
	id             BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
	case_id        BIGINT          UNIQUE,
  resource_type  VARCHAR(255),
	engaged_until  TIMESTAMP,
	hours          FLOAT,
  remark         VARCHAR(512),
  created_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
