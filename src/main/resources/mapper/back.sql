create table sys_sub_param
(
    id          int(20) auto_increment
        primary key,
    param_id    int(30)                             not null,
    param       varchar(30)                         not null,
    create_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);
create table sys_price
(
    id         int(30) auto_increment
        primary key,
    prod_id    int(30)     not null,
    price      double      null,
    spec_param varchar(30) null
);
create table sys_param
(
    id         int(30) auto_increment
        primary key,
    prod_id    int(20)              null,
    param_name varchar(30)          null,
    status     tinyint(1) default 1 null
);
