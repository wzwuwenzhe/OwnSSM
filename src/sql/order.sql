-- 创建订单表
CREATE TABLE `store_order` (
  `id` char(20) NOT NULL  COMMENT '订单ID',
  `cus_id` char(32) NOT NULL  COMMENT '客户ID',
  `operator_id` char(32) NOT NULL COMMENT '操作员ID',
  `store_id` char(8) NOT NULL COMMENT '店铺ID',
  `small_count` varchar(20) NOT NULL COMMENT '小计',
  `discount` varchar(20) not null default '0' COMMENT '折扣',
  `total_amount` varchar(20) NOT NULL COMMENT '应付金额',
  `remark` varchar(100) null COMMENT '备注',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='订单表'

ALTER TABLE
    deady.store_order ADD (pay_type CHAR(1) DEFAULT '1' NOT NULL)
    /*2017-12-13  添加送货地址 、订单状态 */
ALTER TABLE
	deady.store_order ADD (address VARCHAR(100))
ALTER TABLE
	deady.store_order ADD (state CHAR(1) DEFAULT '2' NOT NULL)
ALTER TABLE
	deady.store_order MODIFY COLUMN discount VARCHAR(20) DEFAULT '0' COMMENT '折扣'
