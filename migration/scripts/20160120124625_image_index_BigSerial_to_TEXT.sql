-- // image index BigSerial to TEXT
-- Migration SQL that makes the change goes here.

ALTER TABLE image DROP COLUMN id;
ALTER TABLE image ADD COLUMN id VARCHAR(8) NOT NULL PRIMARY KEY DEFAULT  SUBSTRING(md5(random()::text), 25);

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE image DROP COLUMN id;
ALTER TABLE image ADD COLUMN id BIGSERIAL PRIMARY KEY;

