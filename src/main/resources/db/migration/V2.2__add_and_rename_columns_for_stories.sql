ALTER TABLE stories
ADD publish BOOLEAN;

ALTER TABLE stories
RENAME COLUMN publishDate TO publish_date;

ALTER TABLE stories
RENAME COLUMN ssFile TO ss_file;
