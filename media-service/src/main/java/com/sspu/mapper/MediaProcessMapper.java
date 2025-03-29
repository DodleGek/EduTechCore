package com.sspu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sspu.entity.MediaProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {
    /**
     * 根据分片参数获取待处理任务
     *
     * @param shardTotal 分片总数
     * @param shardIndex 分片序号
     * @param count      任务数
     * @return
     */
    @Select("SELECT * FROM media_process WHERE id % #{shardTotal} = #{shardIndex} AND status = '1' LIMIT #{count}")
    List<MediaProcess> selectListByShardIndex(@Param("shardTotal") int shardTotal, @Param("shardIndex") int shardIndex, @Param("count") int count);

}
