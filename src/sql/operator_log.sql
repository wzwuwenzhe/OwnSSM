-- 创建操作员日志表
CREATE TABLE `operator_log` (
  `operator_id` char(10) NOT NULL  COMMENT '操作员ID',
  `operator_type`  char(1)  NOT NULL  COMMENT '操作员类型:1超管;2:营业员,3:店主',
  `store_id` char(8) NOT NULL  COMMENT '店铺ID',
  `operator_name` varchar(50) NOT NULL COMMENT '操作员姓名',
  `request_url` varchar(500) NOT NULL COMMENT '请求地址',
  `params` varchar(1000)  NULL  COMMENT '请求参数',
  `operate_time` timestamp NOT NULL  COMMENT '操作时间' 
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='操作日志表'

