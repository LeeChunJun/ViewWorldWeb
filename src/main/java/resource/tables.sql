create database music_all_db;
use music_all_db;

create table users (
	id bigint not null auto_increment,
	name varchar(100) not null,
	email varchar(100) not null,
	password varchar(100) not null,
	phone varchar(36) not null,
	tags varchar(512) default null,
--	...more user information...
	primary key (id)
);

create table items (
	id bigint not null auto_increment,
	song_name varchar(256) not null,
	singer_name varchar(256) not null,
	pic_url varchar(256) not null,
	publish_time varchar(12) not null,
	lyric text default null,
	tags varchar(512) default null,
--	...more user information...
	primary key (id)	
);

create table rates (
	userID bigint not null,
	itemID bigint not null,
	preference float not null default 0,
	timestamp integer not null default 0,
	foreign key (userID) references users(id) on delete cascade,
	foreign key (itemID) references items(id) on delete cascade
);

create table item_similarities (
	itemID1 bigint not null,
	itemID2 bigint not null,
	similarity double not null default 0,
	foreign key (itemID1) references items(id) on delete cascade,
	foreign key (itemID2) references items(id) on delete cascade
);

create table line_total (
	id int not null,
	tableName varchar(12) not null,
	timestamp long not null default 0,
	sum integer not null default 0,
	primary key (id)
);

create index rates_index1 on rates ( userID , itemID );
create index rates_index2 ON rates ( userID );
create index rates_index3 ON rates ( itemID );
