package com.lld.im.common.model.message;

import com.lld.im.common.model.ClientInfo;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/10 18:12
 **/
@Data
public class RecallMessageContent extends ClientInfo {

    private Long messageKey;

    private String fromId;

    private String toId;

    private Long messageTime;

    private Long messageSequence;

    private Integer conversationType;
}
