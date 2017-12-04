create table elo_rating (playerId int, eloRating double, gameId int, id int,
PRIMARY KEY ( id ));

create table feedback (id int, player_id int, feedback_text longtext,
PRIMARY KEY ( id ));

create table ping_pong_game (gameId int, player1Id int, player2Id int, score1 int, score2 int,
date timestamp, deleted int, PRIMARY KEY (gameId));

create table player (id int, username varchar(100), deleted int, first_name varchar(100), last_name VARCHAR (100),
PRIMARY KEY (id));