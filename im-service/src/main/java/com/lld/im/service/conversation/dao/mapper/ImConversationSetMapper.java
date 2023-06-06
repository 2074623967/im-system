package com.lld.im.service.conversation.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lld.im.service.conversation.dao.ImConversationSetEntity;
import org.apache.ibatis.annotations.Param;

public interface ImConversationSetMapper extends BaseMapper<ImConversationSetEntity> {

    void readMark(@Param("imConversationSetEntity") ImConversationSetEntity imConversationSetEntity);
}
