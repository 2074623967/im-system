package com.lld.message.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lld.message.dao.ImMessageHistoryEntity;


import java.util.Collection;

public interface ImMessageHistoryMapper extends BaseMapper<ImMessageHistoryEntity> {

    /**
     * 批量插入（mysql）
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(Collection<ImMessageHistoryEntity> entityList);
}