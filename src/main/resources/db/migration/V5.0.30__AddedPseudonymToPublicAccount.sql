ALTER TABLE public_account
    ADD pseudonym varchar(255);

ALTER TABLE public_account
    ADD pseudonym_used bit not null;
