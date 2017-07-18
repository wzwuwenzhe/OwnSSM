-- 创建店铺信息表
CREATE TABLE `store` (
  `id` char(8) NOT NULL  COMMENT '店铺ID',
  `name` varchar(50) NOT NULL COMMENT '店铺名称',
  `address` varchar(100) NOT NULL COMMENT '店铺地址',
  `telephone` varchar(20)  COMMENT '固定电话',
  `mobilephone` varchar(13)  COMMENT '手机号码',
  `status` int(1) default 1 NOT NULL COMMENT '店铺状态(1正常,0停用)',
  `reminder` varchar(100)   COMMENT '温馨提示',
  `logo_img` varchar(100)   COMMENT 'logo图片存储路径',
  `wx_add_img` varchar(100)   COMMENT '微信添加好友二维码图片存储路径',
  `wx_pay_img` varchar(100)   COMMENT '微信支付二维码图片存储路径',
  `zfb_pay_img` varchar(100)  COMMENT '支付宝支付二维码图片存储路径',
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='店铺信息表'