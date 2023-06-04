package com.lld.im.common.model.message;

import com.lld.im.common.model.ClientInfo;
import lombok.Data;

import java.io.PipedReader;

/**
 * @author tangcj
 * @date 2023/06/04 10:16
 **/
@Data
public class MessageContent extends ClientInfo {

    private String messageId;

    private String fromId;

    private String toId;

    private String messageBody;

    private Long messageTime;

    private Long createTime;

    private String extra;

    private Long messageKey;

    private Long messageSequence;
}
