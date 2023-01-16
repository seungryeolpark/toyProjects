insert into member (member_id, username, password) values (0, 'ADMIN', '$2a$10$3/KhKOQNcVfqHpWRyX8HpOh6p53YgPy2vAFcDmXDsR4SWR6GR0hVa');
insert into authority (authority_name) values ('ROLE_ADMIN');
insert into member_authority (member_authority_id, member_id, authority_id) values (0, 0, 'ROLE_ADMIN');