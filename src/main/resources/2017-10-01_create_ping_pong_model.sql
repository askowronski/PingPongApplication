create table Player (
id INTEGER not null,
username VARCHAR(255) not null,
deleted number(1)
);

create SEQUENCE PLAYER_seq
  start with 1
  increment by 1
  nocache
  nocycle;