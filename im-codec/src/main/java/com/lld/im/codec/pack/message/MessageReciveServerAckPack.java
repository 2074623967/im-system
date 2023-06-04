package com.lld.im.codec.pack.message;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 18:37
 **/
@Data
public class MessageReciveServerAckPack {

    private Long messageKey;

    private String fromId;

    private String toId;

    private Long messageSequence;

    private Boolean serverSend;
}
