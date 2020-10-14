ALTER TABLE public_account
    ADD name_hidden bit not null;

ALTER TABLE public_account
    ADD pseudonym varchar(255) not null;