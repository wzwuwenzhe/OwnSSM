-- 创建客户价格表
CREATE TABLE `client` (
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `id` char(32) NOT NULL  COMMENT '客户id',
  `name` varchar(50) NOT NULL COMMENT '款号',
  `price` varchar(13) COMMENT '售价',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
  PRIMARY KEY (`store_id`,`id`,`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='客户信息表'

