package com.lld.message.model;

import com.lld.im.common.model.message.GroupChatMessageContent;
import com.lld.message.dao.ImMessageBodyEntity;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 16:31
 **/
@Data
public class DoStoreGroupMessageDto {

    private GroupChatMessageContent groupChatMessageContent;

    private ImMessageBodyEntity imMessageBodyEntity;
}
