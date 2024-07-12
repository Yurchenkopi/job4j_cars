ALTER TABLE files DROP COLUMN path;
ALTER TABLE files ADD COLUMN path varchar NOT NULL default 'files/001.jpg';
