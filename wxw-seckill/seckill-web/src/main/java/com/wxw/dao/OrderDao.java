package com.wxw.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxw.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDao extends BaseMapper<Order> {

}