-- 创建入库表
CREATE TABLE `stock` (
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `name` varchar(50) NOT NULL COMMENT '款号',
  `year` varchar(4) NOT NULL COMMENT '年份',
  `factory_id` varchar(32) NOT NULL COMMENT '厂家ID',
  `amount` varchar(13) NOT NULL COMMENT '单次入库数量',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `color` varchar(100) NOT NULL DEFAULT '黑色,白色' COMMENT '所有颜色',
  `size` varchar(100) NOT NULL DEFAULT 'X,L,XL' COMMENT '所有尺码'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='入库表'


ALTER TABLE
    deady.stock ADD (colors VARCHAR(100) DEFAULT '黑色,白色' NOT NULL);
ALTER TABLE
    deady.stock ADD (sizes VARCHAR(100) DEFAULT 'M,L,XL' NOT NULL)
