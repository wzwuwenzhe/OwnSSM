-- 创建客户表
CREATE TABLE `client` (
  `id` char(32) NOT NULL  COMMENT '客户id',
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `name` varchar(50) NOT NULL COMMENT '客户姓名',
  `phone` varchar(13) COMMENT '手机号码',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='客户信息表'

ALTER TABLE
    deady.client MODIFY COLUMN phone VARCHAR(13) COMMENT '手机号码'