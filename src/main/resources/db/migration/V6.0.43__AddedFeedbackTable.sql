create table if not exists feedback
(
    id         bigint    not null auto_increment,
    content    TEXT      not null,
    user_info  varchar(255),
    created_at timestamp not null,
    reviewed   bit       not null,
    active     bit       not null,
    primary key (id)
);