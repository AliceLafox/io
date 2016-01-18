-- // remove active field from images
-- Migration SQL that makes the change goes here.

ALTER TABLE image DROP COLUMN active;


-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE image ADD COLUMN active boolean  NOT NULL DEFAULT true;
