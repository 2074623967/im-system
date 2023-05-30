package com.lld.im.codec.proto;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/30 12:25
 **/
@Data
public class Message {

    private MessageHeader messageHeader;

    private Object messagePack;

    @Override
    public String toString() {
        return "Message{" +
                "messageHeader=" + messageHeader +
                ", messagePack=" + messagePack +
                '}';
    }
}
