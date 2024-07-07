ALTER TABLE cars ADD COLUMN vin_num VARCHAR(17) UNIQUE;
ALTER TABLE cars ADD COLUMN color_id int references colors(id) on delete cascade;
ALTER TABLE cars ADD COLUMN model_id int references models(id) on delete cascade;



