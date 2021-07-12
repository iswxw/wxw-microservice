package com.wxw.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * t_sec_kill
 * @author 
 */
@Data
@TableName("t_sec_kill")
public class SecKill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品库存id
     */
    @TableId(type = IdType.AUTO)
    private Long seckillId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 库存数量
     */
    private Integer number;

    /**
     * 秒杀开启时间
     */

    private Timestamp startTime;

    /**
     * 秒杀结束时间
     */
    private Timestamp endTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 版本号
     */
    private Integer version;
}