ALTER TABLE history DROP COLUMN start_at;
ALTER TABLE history DROP COLUMN end_at;

ALTER TABLE history ADD COLUMN start_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE history ADD COLUMN end_at TIMESTAMP WITHOUT TIME ZONE;
