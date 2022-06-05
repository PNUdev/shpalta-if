create table if not exists header_link
(
    id              bigint       not null auto_increment,
    name            varchar(255) not null,
    link            varchar(255) not null,
    sequence        bigint       not null,
    open_in_new_tab boolean,
    primary key (id)
);
