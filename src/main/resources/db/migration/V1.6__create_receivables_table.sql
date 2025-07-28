CREATE TABLE receivables (
	id                BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fund_id           BIGINT          UNIQUE,
  reference_number  VARCHAR(255)    UNIQUE,
	receiving_date    TIMESTAMP,
	amount            FLOAT,
  remark            VARCHAR(512),
  created_at        TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at        TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
