insert into GENRES values (random_uuid(), 'Fiction');
insert into AUTHORS values (random_uuid(), 'Author');
insert into BOOKS values (random_uuid(), 'Book', (select id from GENRES), DEFAULT);
insert into BOOKS_AUTHORS values ((select id from BOOKS), (select id from AUTHORS));

insert into AUTHORITIES values (random_uuid(), 'ROLE_ADMIN');
insert into AUTHORITIES values (random_uuid(), 'ROLE_USER');