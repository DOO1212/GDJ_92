create user 'user01'@'%' identified by 'user01';

create database user01;

grant all privileges on user01.* to 'user01'@'%';

flush privileges;

commit;