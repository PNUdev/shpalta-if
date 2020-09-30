create table if not exists category
(
  id          bigint       not null auto_increment,
  color_theme varchar(255) not null,
  title       varchar(255) not null,
  primary key (id)
);

create table if not exists post
(
  id                       bigint       not null auto_increment,
  title                    varchar(255) not null,
  author_public_account_id bigint       not null,
  category_id              bigint       not null,
  primary key (id)
);

create table if not exists public_account
(
  id                bigint       not null auto_increment,
  name              varchar(255) not null,
  surname           varchar(255) not null,
  profile_image_url varchar(255),
  description       varchar(255),
  created_at        datetime,
  updated_at        datetime,
  user_id           bigint,
  primary key (id)
);

create table if not exists user
(
  id       bigint       not null auto_increment,
  active   bit          not null,
  password varchar(255) not null,
  role     varchar(255) not null,
  username varchar(255) not null,
  primary key (id)
);

alter table user
  add constraint UQ_User_Username unique (username);

alter table post
  add constraint FK_Post_Public_account foreign key (author_public_account_id) references public_account (id);

alter table post
  add constraint FK_Post_Category foreign key (category_id) references category (id);

alter table public_account
  add constraint FK_Public_account_User foreign key (user_id) references user (id);