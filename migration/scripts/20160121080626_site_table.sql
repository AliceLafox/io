-- // site table
-- Migration SQL that makes the change goes here.
CREATE TABLE site(
   name text NOT NULL PRIMARY KEY
);

INSERT INTO site (name) VALUES ('test-domain');

delete from token where site_name != 'test-domain';

ALTER TABLE token
ADD CONSTRAINT token_site_name_fkey FOREIGN KEY (site_name) REFERENCES site (name) ON UPDATE CASCADE ON DELETE RESTRICT;

CREATE TABLE site_ip(
  id bigserial PRIMARY KEY,
  site_name text NOT NULL,
  network inet NOT NULL
);
INSERT INTO site_ip (site_name, network)  VALUES ('test-domain','127.0.0.1/24');
INSERT INTO site_ip (site_name, network)  VALUES ('test-domain','10.0.0.0/8');
INSERT INTO site_ip (site_name, network)  VALUES ('test-domain','172.16.0.0/12');
INSERT INTO site_ip (site_name, network)  VALUES ('test-domain','192.168.0.0/16');

ALTER TABLE site_ip
ADD CONSTRAINT site_ip_site_name_fkey  FOREIGN KEY (site_name) REFERENCES site (name) ON UPDATE CASCADE ON DELETE RESTRICT;

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE token DROP CONSTRAINT token_site_name_fkey;
ALTER TABLE site_ip DROP CONSTRAINT site_ip_site_name_fkey;
DROP TABLE IF EXISTS site_ip;
DROP TABLE IF EXISTS site;
