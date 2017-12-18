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

--20171218  添加客户送货地址
ALTER TABLE
    deady.client ADD (deliver_address VARCHAR(50))
    
--批量更新client表  为deliver_address 添加数据
UPDATE client AS c1 
INNER JOIN (select c.id,s.address from store_order s , client c where s.cus_id=c.id and s.address <>'') AS c2 
SET c1.deliver_address = c2.address WHERE c1.id = c2.id