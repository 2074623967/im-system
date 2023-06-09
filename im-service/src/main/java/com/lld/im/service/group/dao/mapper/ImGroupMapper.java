package com.lld.im.service.group.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lld.im.service.group.dao.ImGroupEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface ImGroupMapper extends BaseMapper<ImGroupEntity> {

    Long getGroupMaxSeq(@Param("groupId") Collection<String> groupId,@Param("appId") Integer appId);
}
