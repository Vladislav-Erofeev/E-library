--changeset vladislav:1

create table liked_book(
   person_id int references person(id) on delete cascade ,
   book_id int references book(id) on delete cascade ,
   primary key (person_id, book_id)
)