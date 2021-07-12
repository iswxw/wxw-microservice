package com.wxw.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxw.domain.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockDao extends BaseMapper<Stock> {

}