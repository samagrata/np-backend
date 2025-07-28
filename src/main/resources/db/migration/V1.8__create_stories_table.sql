CREATE TABLE stories (
	id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
	case_id          BIGINT          UNIQUE,
  title            VARCHAR(255),
  paras            TEXT,
  carousel_images  VARCHAR(512),
  carousels        VARCHAR(512),
	publishDate      TIMESTAMP,
  ssFile           VARCHAR(255),
  created_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
