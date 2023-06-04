package com.lld.im.common.model.message;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 16:04
 **/
@Data
public class DoStoreGroupMessageDto {

    private GroupChatMessageContent groupChatMessageContent;

    private ImMessageBody messageBody;
}
