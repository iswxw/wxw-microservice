package com.wxw.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxw.domain.SecKill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SecKillDao extends BaseMapper<SecKill> {

    void updateNumberBySecKillId(long secKillId);

    Long queryNumberBySeckill(long killId);

    void updateNumberByKillId(long killId);

    Long queryNumberBySeckillAndPessimistic(long killId);

    int updateNumberByKillIdAndNumber(long killId);

    int updateStockNumberByVersion(@Param("killId") long killId, @Param("number") long number, @Param("version") Integer version);

    void updateVersionBySecKillId(long secKillId);
}