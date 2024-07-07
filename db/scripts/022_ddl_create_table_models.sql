create table models(
                           id serial primary key,
                           name varchar,
                           manufacturer_id int references manufacturers(id),
                           category_id int references categories(id),
                           body_type_id int references body_types(id)
);