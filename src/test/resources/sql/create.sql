DROP TABLE IF EXISTS member;
CREATE TABLE member
(
    id                    bigint auto_increment comment '멤버 ID'
        primary key,
    name                  varchar(50)                        not null,
    team_id               bigint                             not null
)
    comment '멤버';

DROP TABLE IF EXISTS team;
CREATE TABLE team
(
    id                    bigint auto_increment comment '팀 ID'
        primary key,
    name                  varchar(50)                        not null
)
    comment '팀';
