alter table post
    ADD content     TEXT    not null,
    ADD picture_url varchar(255),
    ADD active      bit     not null,
    ADD created_at  timestamp


