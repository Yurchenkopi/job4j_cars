create table cars(
                    id serial primary key,
                    engine_id int not null unique references engines(id) on delete cascade
);