-- 创建订单项目表
CREATE TABLE `item` (
  `id` char(32) NOT NULL  COMMENT '单条记录ID',
  `order_id` char(20) NOT NULL  COMMENT '订单Id',
  `name` varchar(50) NOT NULL COMMENT '商品名称',
  `color` varchar(50) NOT NULL COMMENT '商品颜色' DEFAULT '黑色',
  `size` varchar(10) NOT NULL COMMENT '尺寸' DEFAULT 'L',
  `unit_price` varchar(20) NOT NULL COMMENT '单价',
  `amount` varchar(10) NOT NULL COMMENT '数量',
  `price` varchar(20) NOT NULL COMMENT '单条记录总金额',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='订单项目表'

ALTER TABLE
    deady.item ADD (color VARCHAR(50) DEFAULT '黑色' NOT NULL)