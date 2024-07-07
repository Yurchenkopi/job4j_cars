ALTER TABLE cars_owners ADD COLUMN history_id int references history(id);
ALTER TABLE cars_owners ADD COLUMN reg_num_id int references reg_nums(id);