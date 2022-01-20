CREATE DATABASE IF NOT EXISTS shop;

CREATE TABLE shop.user (
    id bigint auto_increment comment '유저 ID' primary key,
    name varchar(50) not null
);

create user masteruser@'%' identified by 'masterpassword';
grant all privileges on *.* to masteruser@'%' identified by 'masterpassword';

grant replication slave on *.* to 'slaveuser'@'%' identified by 'slavepassword';
