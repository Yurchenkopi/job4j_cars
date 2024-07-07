ALTER TABLE auto_post ADD COLUMN status boolean;
ALTER TABLE auto_post ADD COLUMN file_id int unique references files(id) on delete cascade;
