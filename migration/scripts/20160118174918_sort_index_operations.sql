-- // sort index operations
-- Migration SQL that makes the change goes here.

CREATE OR REPLACE FUNCTION sort_index_minus(_id bigint) RETURNS void AS $BODY$
DECLARE
  _token_id    BIGINT;
  _sort_index  BIGINT;
  _sort_index2 BIGINT;
  _id2         BIGINT;
BEGIN
  SELECT token_id, sort_index INTO _token_id, _sort_index FROM image WHERE id = _id;
  SELECT id, sort_index INTO _id2, _sort_index2 FROM image WHERE token_id = _token_id AND sort_index < _sort_index  ORDER BY sort_index DESC LIMIT 1;
  IF FOUND THEN
    UPDATE image SET sort_index = _sort_index  WHERE id = _id2;
    UPDATE image SET sort_index = _sort_index2 WHERE id = _id;
  END IF;
END $BODY$  LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION sort_index_plus(_id bigint) RETURNS void AS $BODY$
DECLARE
  _token_id    BIGINT;
  _sort_index  BIGINT;
  _sort_index2 BIGINT;
  _id2         BIGINT;
BEGIN
  SELECT token_id, sort_index INTO _token_id, _sort_index FROM image WHERE id = _id;
  SELECT id, sort_index INTO _id2, _sort_index2 FROM image WHERE token_id = _token_id AND sort_index > _sort_index ORDER BY sort_index ASC LIMIT 1;
  IF FOUND THEN
    UPDATE image SET sort_index = _sort_index  WHERE id = _id2;
    UPDATE image SET sort_index = _sort_index2 WHERE id = _id;
  END IF;
END $BODY$ LANGUAGE plpgsql;




CREATE OR REPLACE FUNCTION sort_index_to_first(_id bigint) RETURNS void AS $BODY$
DECLARE
  _token_id       BIGINT;
  _min_sort_index BIGINT;
  _min_id         BIGINT;
BEGIN
  SELECT token_id INTO _token_id FROM image WHERE id = _id;
  SELECT id INTO _min_id FROM image WHERE token_id = _token_id ORDER BY sort_index ASC LIMIT 1;
  SELECT sort_index INTO _min_sort_index FROM image WHERE token_id = _token_id AND id != _id ORDER BY sort_index ASC LIMIT 1;
  IF FOUND AND _id != _min_id THEN
    UPDATE image SET sort_index = _min_sort_index - 1 WHERE id = _id;
  END IF;
END $BODY$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sort_index_to_last(_id bigint) RETURNS void AS $BODY$
DECLARE
  _token_id       BIGINT;
  _max_sort_index BIGINT;
  _max_id         BIGINT;
BEGIN
  SELECT token_id INTO _token_id FROM image WHERE id = _id;
  SELECT id INTO _max_id FROM image WHERE token_id = _token_id ORDER BY sort_index DESC LIMIT 1;
  SELECT sort_index INTO _max_sort_index FROM image WHERE token_id = _token_id AND id != _id ORDER BY sort_index DESC LIMIT 1;
  IF FOUND AND _id != _max_id THEN
    UPDATE image SET sort_index = _max_sort_index + 1 WHERE id = _id;
  END IF;
END $BODY$ LANGUAGE plpgsql;

-- //@UNDO
-- SQL to undo the change goes here.

DROP FUNCTION IF EXISTS sort_index_minus(bigint);
DROP FUNCTION IF EXISTS sort_index_plus(bigint);
DROP FUNCTION IF EXISTS sort_index_to_first(bigint);
DROP FUNCTION IF EXISTS sort_index_to_last(bigint);
