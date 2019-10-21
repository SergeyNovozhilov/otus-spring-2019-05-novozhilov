insert into AUTHORS values (random_uuid(), 'Author');
insert into BOOKS values (random_uuid(), 'Book', (select id from GENRES), DEFAULT);
insert into BOOKS_AUTHORS values ((select id from BOOKS), (select id from AUTHORS));

insert into USERS values (random_uuid(), '$2a$10$hQTcCd4DrWihRKZc6AUN5eOy/PBK8XlnVME.jaIvNrTJhtko6Lyyi', 'admin');
insert into USERS values (random_uuid(), '$2a$10$dpTW60IbTjh9tsP.o2SDp.nBhjScIPKMNgjrwAUZqAw.EOMzFVTta', 'user');

insert into AUTHORITIES values (random_uuid(), 'ROLE_ADMIN');
insert into AUTHORITIES values (random_uuid(), 'ROLE_USER');

insert into USERS_AUTHORITIES values ((select  id from users where username='admin'), (select id from AUTHORITIES where name='ROLE_ADMIN'));
insert into USERS_AUTHORITIES values ((select  id from users where username='admin'), (select id from AUTHORITIES where name='ROLE_USER'));
insert into USERS_AUTHORITIES values ((select  id from users where username='user'), (select id from AUTHORITIES where name='ROLE_USER'));