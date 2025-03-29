package com.sspu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sspu.entity.MqMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MqMessageMapper extends BaseMapper<MqMessage> {

}
