CREATE DATABASE IF NOT EXISTS shop;

CREATE TABLE shop.user (
    id bigint auto_increment comment '유저 ID' primary key,
    name varchar(50) not null
);

create user slaveuser@'%' identified by 'slavepassword';
grant all privileges on shop.* to slaveuser@'%' identified by 'slavepassword';

grant replication slave on *.* to 'slaveuser'@'%' identified by 'slavepassword';