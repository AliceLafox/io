-- // sort index triger
-- Migration SQL that makes the change goes here.

CREATE OR REPLACE FUNCTION set_sort_index() RETURNS trigger AS $BODY$
DECLARE
  _max_sort_index BIGINT;
  _token_id BIGINT;
BEGIN
  SELECT max(sort_index) INTO _max_sort_index FROM image WHERE token_id=NEW.token_id;
  IF _max_sort_index is NULL THEN
    _max_sort_index = 0;
  END IF;
  NEW.sort_index = _max_sort_index + 1;
  RETURN NEW;
END $BODY$ LANGUAGE plpgsql;

CREATE TRIGGER tf_set_sort_index BEFORE INSERT ON image FOR EACH ROW EXECUTE PROCEDURE set_sort_index();


-- //@UNDO
-- SQL to undo the change goes here.


DROP TRIGGER IF EXISTS tf_set_sort_index ON image;
DROP FUNCTION IF EXISTS set_sort_index();



