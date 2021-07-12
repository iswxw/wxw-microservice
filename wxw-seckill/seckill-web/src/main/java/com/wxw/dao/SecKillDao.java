package com.wxw.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxw.domain.SecKill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SecKillDao extends BaseMapper<SecKill> {

    void updateNumberBySecKillId(long secKillId);

    Long queryNumberBySeckill(long killId);

    void updateNumberByKillId(long killId);
}