package com.lld.im.codec.pack.conversation;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/06 21:59
 **/
@Data
public class UpdateConversationPack {

    private String conversationId;

    private Integer isMute;

    private Integer isTop;

    private Integer conversationType;

    private Long sequence;
}
