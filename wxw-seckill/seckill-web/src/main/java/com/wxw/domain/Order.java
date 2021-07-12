package com.wxw.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_order
 * @author 
 */
@Data
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Integer orderId;

    /**
     * 订单名称

     */
    private String orderName;

    /**
     * 订单描述
     */
    private String orderDescription;

    /**
     * 司机Id
     */
    private Integer skuId;

    /**
     * 创建订单时间
     */
    private Date createTime;
}