--changeset vladislav:1

create table person(
   id int generated by default as identity primary key,
   name varchar(100) not null,
   last_name varchar(100) not null,
   surname varchar(100) not null,
   year int not null,
   email varchar(100) unique,
   role varchar(100) not null,
   password varchar
)