-- 创建入库表
CREATE TABLE `stock` (
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `name` varchar(50) NOT NULL COMMENT '款号',
  `year` varchar(4) NOT NULL COMMENT '年份',
  `factory_id` varchar(32) NOT NULL COMMENT '厂家ID',
  `amount` varchar(13) NOT NULL COMMENT '单次入库数量',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' 
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='入库表'