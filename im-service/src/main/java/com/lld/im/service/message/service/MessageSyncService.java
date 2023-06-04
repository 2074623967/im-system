package com.lld.im.service.message.service;

import com.lld.im.common.enums.command.MessageCommand;
import com.lld.im.common.model.message.MessageReciveAckContent;
import com.lld.im.service.utils.MessageProducer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tangcj
 * @date 2023/06/04 18:23
 **/
@Service
public class MessageSyncService {

    @Resource
    private MessageProducer messageProducer;


    public void receiveMark(MessageReciveAckContent messageReciveAckContent) {
        messageProducer.sendToUser(messageReciveAckContent.getToId(), MessageCommand.MSG_RECIVE_ACK,
                messageReciveAckContent, messageReciveAckContent.getAppId());
    }
}
