ALTER TABLE auto_post
    ADD COLUMN car_id int unique REFERENCES cars(id);