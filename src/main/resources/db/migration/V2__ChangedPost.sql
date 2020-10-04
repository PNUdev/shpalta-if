ALTER TABLE post
    ADD picture_url varchar(255) not null;

ALTER TABLE post
    ADD content     TEXT    not null;

ALTER TABLE post
    ADD active      bit     not null;

ALTER TABLE post
    ADD created_at  timestamp;
