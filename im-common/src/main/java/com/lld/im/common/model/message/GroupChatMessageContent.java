package com.lld.im.common.model.message;

import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/06/04 11:31
 **/
@Data
public class GroupChatMessageContent extends MessageContent{

    private String groupId;

    private List<String> memberId;
}
