drop database mall;
CREATE
    database mall;
USE
    mall;
CREATE TABLE `sys_avatar`
(
    `user_id`      int(20) NOT NULL,
    `avatar_bytes` longblob,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*Data for the table `sys_avatar` */


/*Table structure for table `sys_file_map` */

CREATE TABLE `sys_file_map`
(
    `uid`       int(20)    DEFAULT NULL,
    `file_seq`  varchar(64) NOT NULL,
    `extension` varchar(10) NOT NULL,
    `status`    tinyint(1) DEFAULT NULL COMMENT '0禁用1可用',
    PRIMARY KEY (`file_seq`, `extension`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


/*Table structure for table `sys_menu` */

CREATE TABLE `sys_menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
    `menu_name`   varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '权限名字?',
    `path`        varchar(200)         DEFAULT NULL,
    `component`   varchar(255)         DEFAULT NULL,
    `visible`     char(1)              DEFAULT '0' COMMENT ' 0不可1可?',
    `status`      char(1)              DEFAULT '1' COMMENT ' 0不可用1可用?',
    `perms`       varchar(100)         DEFAULT NULL,
    `icon`        varchar(100)         DEFAULT '#',
    `create_by`   bigint(20)           DEFAULT NULL,
    `create_time` datetime             DEFAULT NULL,
    `update_by`   bigint(20)           DEFAULT NULL,
    `update_time` datetime             DEFAULT NULL,
    `del_flag`    int(11)              DEFAULT '0',
    `remark`      varchar(500)         DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='menu?';

INSERT INTO mall.sys_menu (id, menu_name, path, component, visible, status, perms, icon, create_by, create_time,
                           update_by, update_time, del_flag, remark)
VALUES (1, '普通用户权限', null, null, '0', '1', 'sys.scan', '#', null, null, null, null, 0, null);
INSERT INTO mall.sys_menu (id, menu_name, path, component, visible, status, perms, icon, create_by, create_time,
                           update_by, update_time, del_flag, remark)
VALUES (2, '更新上哦权限', null, null, '0', '1', 'sys.update', '#', null, null, null, null, 0, null);
INSERT INTO mall.sys_menu (id, menu_name, path, component, visible, status, perms, icon, create_by, create_time,
                           update_by, update_time, del_flag, remark)
VALUES (3, '删除商品权限', null, null, '0', '1', 'sys.del', '#', null, null, null, null, 0, null);
INSERT INTO mall.sys_menu (id, menu_name, path, component, visible, status, perms, icon, create_by, create_time,
                           update_by, update_time, del_flag, remark)
VALUES (4, '商店拥有者', null, null, '0', '1', 'sys.mowner', '#', null, null, null, null, 0, null);
INSERT INTO mall.sys_menu (id, menu_name, path, component, visible, status, perms, icon, create_by, create_time,
                           update_by, update_time, del_flag, remark)
VALUES (5, '浏览商店商品', null, null, '0', '1', 'sys.mscan', '#', null, null, null, null, 0, null);


/*Table structure for table `sys_merchant` */

CREATE TABLE `sys_merchant`
(
    `id`        int(10)     NOT NULL AUTO_INCREMENT,
    `uid`       int(10)    DEFAULT NULL,
    `status`    tinyint(1) DEFAULT '1',
    `score`     double     DEFAULT NULL,
    `name`      varchar(10) NOT NULL,
    `own_phone` int(11)    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


/*Table structure for table `sys_role` */

CREATE TABLE `sys_role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(128) DEFAULT NULL,
    `role_key`    varchar(100) DEFAULT NULL,
    `status`      char(1)      DEFAULT '0',
    `del_flag`    int(1)       DEFAULT '0' COMMENT 'del_flag',
    `create_by`   bigint(200)  DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_by`   bigint(200)  DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `remark`      varchar(500) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;


INSERT INTO mall.sys_role (id, name, role_key, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES (1, 'admin', 'admin', '0', 0, null, null, null, null, null);
INSERT INTO mall.sys_role (id, name, role_key, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES (2, 'user', 'user', '0', 0, null, null, null, null, null);
INSERT INTO mall.sys_role (id, name, role_key, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES (3, 'merchant', 'merchant', '0', 0, null, null, null, null, null);


/*Table structure for table `sys_role_menu` */

CREATE TABLE `sys_role_menu`
(
    `role_id` bigint(200) NOT NULL AUTO_INCREMENT,
    `menu_id` bigint(200) NOT NULL DEFAULT '0',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;

/*Data for the table `sys_role_menu` */

insert into `sys_role_menu`(`role_id`, `menu_id`)
values (1, 6);
insert into `sys_role_menu`(`role_id`, `menu_id`)
values (2, 1);
insert into `sys_role_menu`(`role_id`, `menu_id`)
values (3, 1);
insert into `sys_role_menu`(`role_id`, `menu_id`)
values (3, 2);
insert into `sys_role_menu`(`role_id`, `menu_id`)
values (3, 3);
insert into `sys_role_menu`(`role_id`, `menu_id`)
values (3, 4);
insert into `sys_role_menu`(`role_id`, `menu_id`)
values (3, 5);

/*Table structure for table `sys_user` */

CREATE TABLE `sys_user`
(
    `id`          int(64)     NOT NULL AUTO_INCREMENT,
    `user_name`   varchar(64) NOT NULL DEFAULT 'NULL',
    `nick_name`   varchar(64)          DEFAULT 'NULL',
    `password`    varchar(64) NOT NULL DEFAULT 'NULL',
    `status`      char(1)              DEFAULT '0',
    `email`       varchar(64)          DEFAULT NULL,
    `phonenumber` varchar(32)          DEFAULT NULL,
    `sex`         char(1)              DEFAULT NULL,
    `avatar`      varchar(128)         DEFAULT NULL,
    `user_type`   char(1)     NOT NULL DEFAULT '1',
    `create_by`   bigint(20)           DEFAULT NULL,
    `create_time` datetime             DEFAULT NULL,
    `update_by`   bigint(20)           DEFAULT NULL,
    `update_time` datetime             DEFAULT NULL,
    `del_flag`    int(11)              DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

/*Data for the table `sys_user` pass 123456 */

insert into `sys_user`(`id`, `user_name`, `nick_name`, `password`, `status`, `email`, `phonenumber`, `sex`, `avatar`,
                       `user_type`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`)
values (null, 'admin', 'NULL', '$2a$10$hvrXH3RbRl17.eqvbVnh7uBgw0pHfwIotYVUFMC7yUrMyxKcedBee', '0', NULL, NULL, NULL,
        NULL,
        '1', NULL, NULL, NULL, NULL, 0);

/*Table structure for table `sys_user_role` */

CREATE TABLE `sys_user_role`
(
    `user_id` bigint(200) NOT NULL AUTO_INCREMENT,
    `role_id` bigint(200) NOT NULL DEFAULT '0',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = MyISAM
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;

/*Data for the table `sys_user_role` */
INSERT INTO mall.sys_user_role (user_id, role_id) VALUES (1, 1);

/*Table structure for table `t_pd_class` */

CREATE TABLE `t_pd_class`
(
    `cid`        int(20)     NOT NULL AUTO_INCREMENT,
    `pid`        int(20)     NOT NULL,
    `mask_sec`   int(1)      NOT NULL,
    `class_name` varchar(30) NOT NULL,
    PRIMARY KEY (`cid`)
) ENGINE = MyISAM
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


/*Table structure for table `t_pd_order` */

CREATE TABLE `t_pd_order`
(
    `oid`            int(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
    `uid`            int(32)                   NOT NULL,
    `num`            int(32)                   NOT NULL,
    `time`           timestamp                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_payed`       tinyint(1)                NOT NULL,
    `sku_serialized` blob,
    PRIMARY KEY (`oid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


/*Table structure for table `t_pd_sku` */

CREATE TABLE `t_pd_sku`
(
    `pid`   int(10)     NOT NULL,
    `mask`  varchar(15) NOT NULL,
    `img`   blob,
    `stock` int(10)     NOT NULL DEFAULT '0',
    `price` double               DEFAULT '0'
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8;


/*Table structure for table `t_pd_tag` */

CREATE TABLE `t_pd_tag`
(
    `cid`     int(20) unsigned NOT NULL,
    `tag_sec` int(1) unsigned  NOT NULL,
    `content` varchar(30)      NOT NULL
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8;

/*Data for the table `t_pd_tag` */


/*Table structure for table `t_prod` */

CREATE TABLE `t_prod`
(
    `id`          int(10)     NOT NULL AUTO_INCREMENT,
    `mid`         int(10)              DEFAULT NULL COMMENT 'pid',
    `prod_name`   varchar(30) NOT NULL,
    `status`      tinyint(1)  NOT NULL DEFAULT '0',
    `img`         text,
    `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `sec_flag`    tinyint(1)           DEFAULT '1',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


/*Table structure for table `test` */

CREATE TABLE `test`
(
    `id`   int(10) NOT NULL AUTO_INCREMENT,
    `name` varchar(20)   DEFAULT NULL,
    `pass` varbinary(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*Data for the table `test` */

/* Trigger structure for table `sys_user` */

# DELIMITER $$
#
# TRIGGER 'BingingRole' AFTER
# INSERT ON 'sys_user' FOR EACH ROW
# insert into sys_user_role(user_id, role_id)
# values (new.id, 2) */$$
#
#
# DELIMITER ;


