-- 创建操作员表
CREATE TABLE `operator` (
  `id` char(10) NOT NULL  COMMENT '操作员ID',
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `name` varchar(50) NOT NULL COMMENT '操作员姓名',
  `phone` varchar(13) NOT NULL COMMENT '手机号码',
  `user_type` char(1) default '1' NOT NULL  COMMENT '操作员类型:-1超管;1:营业员,2:店主',
  `login_name` varchar(20) NOT NULL  COMMENT '登录名',
  `pwd` varchar(100) NOT NULL  COMMENT '加密后的密码',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='操作员表'

