create table cars_owners(
                               id serial primary key,
                               owner_id int not null references owners(id) on delete cascade,
                               car_id int not null references cars(id) on delete cascade,
                               UNIQUE (car_id, owner_id)
);