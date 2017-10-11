-- 创建工厂表
CREATE TABLE `factory` (
  `id` char(32) NOT NULL  COMMENT '工厂id',
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `name` varchar(50) NOT NULL COMMENT '工厂名称姓名',
  `phone` varchar(13) NOT NULL COMMENT '工厂联系电话',
  `address` varchar(100) NULL COMMENT '工厂地址',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='工厂信息表'