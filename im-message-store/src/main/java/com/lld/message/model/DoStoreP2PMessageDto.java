package com.lld.message.model;

import com.lld.im.common.model.message.MessageContent;
import com.lld.message.dao.ImMessageBodyEntity;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 16:28
 **/
@Data
public class DoStoreP2PMessageDto {

    private MessageContent messageContent;

    private ImMessageBodyEntity imMessageBodyEntity;
}
