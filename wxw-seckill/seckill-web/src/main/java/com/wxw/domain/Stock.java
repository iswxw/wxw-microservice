package com.wxw.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_stock
 * @author 
 */
@Data
@TableName("t_stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库存Id
     */
    @TableId(type = IdType.AUTO)
    private Integer stockId;

    /**
     * 商品Id
     */
    private Integer skuId;

    /**
     * 库存名称
     */
    private String stockName;

    /**
     * 库存剩余数量
     */
    private Integer stockCount;

    /**
     * 更新时间
     */
    private Date updateTime;
}