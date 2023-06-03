package com.lld.im.service.friendship.model.callback;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 15:58
 **/
@Data
public class AddFriendBlackAfterCallbackDto {

    private String fromId;

    private String toId;
}
