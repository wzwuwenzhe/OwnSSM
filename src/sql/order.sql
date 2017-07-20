-- 创建订单表
CREATE TABLE `store_order` (
  `id` char(20) NOT NULL  COMMENT '订单ID',
  `cus_id` char(32) NOT NULL  COMMENT '客户ID',
  `operator_id` char(32) NOT NULL COMMENT '操作员ID',
  `store_id` char(8) NOT NULL COMMENT '店铺ID',
  `small_count` varchar(20) NOT NULL COMMENT '小计',
  `discount` varchar(20) not null default '0' COMMENT '折扣',
  `amount` varchar(10) NOT NULL COMMENT '数量',
  `total_amount` varchar(20) NOT NULL COMMENT '应付金额',
  `remark` varchar(100) null COMMENT '备注',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='订单表'