
-- 创建数据库
CREATE DATABASE `seckill-test`;

-- 创建表
use seckill-test;

-- `seckill-test`.t_order 订单表
CREATE TABLE `t_order` (
   `order_id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
   `order_name` varchar(100)  DEFAULT '' COMMENT '订单名称',
   `order_description` varchar(200)  DEFAULT NULL COMMENT '订单描述',
   `sku_id` int DEFAULT '0' COMMENT '商品Id',
   `create_time` datetime DEFAULT NULL COMMENT '创建订单时间',
   PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='订单表';

-- `seckill-test`.t_stock definition

CREATE TABLE `t_stock` (
   `stock_id` int NOT NULL AUTO_INCREMENT COMMENT '库存Id',
   `sku_id` int DEFAULT '0' COMMENT '商品Id',
   `stock_name` varchar(100)  DEFAULT '' COMMENT '库存名称',
   `stock_count` int DEFAULT '0' COMMENT '库存剩余数量',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`stock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='库存';

