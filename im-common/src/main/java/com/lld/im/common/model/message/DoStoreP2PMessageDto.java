package com.lld.im.common.model.message;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 15:41
 **/
@Data
public class DoStoreP2PMessageDto {

    private MessageContent messageContent;

    private ImMessageBody messageBody;
}
