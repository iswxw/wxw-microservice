package com.wxw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * t_success_killed
 * @author 
 */
@Data
@TableName("t_success_killed")
public class SuccessKilled implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀商品id
     */
    private Long seckillId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 状态标示：-1指无效，0指成功，1指已付款
     */
    private short state;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}