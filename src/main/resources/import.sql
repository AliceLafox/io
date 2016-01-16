DROP TABLE IF EXISTS token, image;
CREATE TABLE token
(
  id BIGSERIAL NOT NULL PRIMARY KEY,
  created timestamp with time zone NOT NULL DEFAULT now(),
  ip text,
  owner_id bigint NOT NULL,
  owner_name text NOT NULL,
  read_token text NOT NULL UNIQUE,
  write_token text NOT NULL UNIQUE,
  site_name text NOT NULL,
  CONSTRAINT uk_7b7h1kjeu7jlutl05adx72sq5 UNIQUE (site_name, owner_name, owner_id)
);

CREATE TABLE image
(
  id BIGSERIAL NOT NULL PRIMARY KEY,
  created timestamp with time zone NOT NULL DEFAULT now(),
  modified timestamp with time zone NOT NULL DEFAULT now(),
  active boolean NOT NULL DEFAULT true,
  avatar boolean NOT NULL DEFAULT false,
  content_type text,
  description text,
  file_name text,
  width integer NOT NULL,
  height integer NOT NULL,
  size INTEGER NOT NULL ,
  sort_index integer NOT NULL DEFAULT 0,
  title text,
  version integer NOT NULL DEFAULT 0,
  token_id bigint NOT NULL,
  img bytea,
  CONSTRAINT fk_s9jvy93oe9ypncv5vaxy83eky FOREIGN KEY (token_id)
      REFERENCES token (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


DROP FUNCTION IF EXISTS setAvatar(bigint);

CREATE OR REPLACE FUNCTION setAvatar(_id bigint) RETURNS void AS
  $BODY$
    DECLARE
      _token_id BIGINT;
  BEGIN
    SELECT token_id INTO _token_id FROM image WHERE id = _id;
      IF NOT FOUND
      THEN
        RETURN;
      END IF;
    UPDATE image SET avatar=false where token_id=_token_id and id != _id;
    UPDATE image SET avatar=true where id = _id;

  END
  $BODY$
LANGUAGE plpgsql VOLATILE;