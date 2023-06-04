package com.lld.im.common.model.message;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 14:55
 **/
@Data
public class CheckSendMessageReq {

    private String fromId;

    private String toId;

    private Integer appId;

    private Integer command;
}
