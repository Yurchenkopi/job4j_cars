CREATE TABLE auto_post
(
    id              serial primary key,
    description     varchar                                                  not null,
    creates         timestamp                                                not null,
    auto_user_id    int references auto_user (id)         ON DELETE CASCADE   not null
);