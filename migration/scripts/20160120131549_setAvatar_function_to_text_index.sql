-- // setAvatar function to text index
-- Migration SQL that makes the change goes here.
DROP FUNCTION IF EXISTS setavatar(bigint);

CREATE OR REPLACE FUNCTION setavatar(_id TEXT) RETURNS void AS $BODY$
  DECLARE
    _token_id BIGINT;
  BEGIN
    SELECT token_id INTO _token_id FROM image WHERE id = _id;
    IF FOUND THEN
      UPDATE image SET avatar=false where token_id=_token_id and id != _id;
      UPDATE image SET avatar=true where id = _id;
    END IF;
  END
$BODY$ LANGUAGE plpgsql;


-- //@UNDO
-- SQL to undo the change goes here.

DROP FUNCTION IF EXISTS setavatar(TEXT);

CREATE OR REPLACE FUNCTION setavatar(_id bigint) RETURNS void AS $BODY$
  DECLARE
    _token_id BIGINT;
  BEGIN
    SELECT token_id INTO _token_id FROM image WHERE id = _id;
    IF FOUND THEN
      UPDATE image SET avatar=false where token_id=_token_id and id != _id;
      UPDATE image SET avatar=true where id = _id;
    END IF;
  END
$BODY$ LANGUAGE plpgsql;
