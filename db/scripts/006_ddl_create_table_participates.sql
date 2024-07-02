CREATE TABLE participates (
                              id serial PRIMARY KEY,
                              user_id int not null REFERENCES auto_user(id) on delete cascade,
                              post_id int not null REFERENCES auto_post(id) on delete cascade,
                              UNIQUE (user_id, post_id)
);