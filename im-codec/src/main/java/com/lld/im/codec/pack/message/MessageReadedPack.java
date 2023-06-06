package com.lld.im.codec.pack.message;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/05 21:34
 **/
@Data
public class MessageReadedPack {

    private long messageSequence;

    private String fromId;

    private String groupId;

    private String toId;

    private Integer conversationType;
}
