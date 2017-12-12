-- 创建库存表
CREATE TABLE `storage` (
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `name` varchar(50) NOT NULL COMMENT '款号',
  `year` varchar(4) NOT NULL COMMENT '年份',
  `total` varchar(13) NOT NULL COMMENT '总计',
  `stock_left` varchar(13) NOT NULL COMMENT '剩下库存',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
  `colors` varchar(100) NOT NULL default '黑色,白色',
  `sizes` varchar(100) NOT NULL default 'M,L,XL'
  PRIMARY KEY (`store_id`,`name`,`year`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='库存表'


ALTER TABLE
    deady.storage ADD (colors VARCHAR(100) DEFAULT '黑色,白色' NOT NULL);
ALTER TABLE
    deady.storage ADD (sizes VARCHAR(100) DEFAULT 'M,L,XL' NOT NULL)