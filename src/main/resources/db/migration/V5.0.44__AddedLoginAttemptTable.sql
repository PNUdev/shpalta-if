create table if not exists login_attempt
(
    id         bigint       not null auto_increment,
    ip_address varchar(255) not null,
    date_time  timestamp    not null,
    success    bit          not null,
    ip_blocked bit          not null,
    primary key (id)
);