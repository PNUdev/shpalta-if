create table if not exists telegram_bot_user
(
  chat_id        bigint       not null,
  settings_token varchar(255) not null,
  primary key (chat_id)
);

create table if not exists telegram_user_category_subscriptions
(
  chat_id     bigint not null,
  category_id bigint not null,
  unique (chat_id, category_id)
);

alter table telegram_user_category_subscriptions
  add constraint FK_SUBSCRIPTION_CHAT_ID foreign key (chat_id) references telegram_bot_user (chat_id);

alter table telegram_user_category_subscriptions
  add constraint FK_SUBSCRIPTION_CATEGORY foreign key (category_id) references category (id)