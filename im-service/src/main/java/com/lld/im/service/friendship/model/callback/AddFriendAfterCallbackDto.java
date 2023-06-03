package com.lld.im.service.friendship.model.callback;

import com.lld.im.service.friendship.model.req.FriendDto;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 15:36
 **/
@Data
public class AddFriendAfterCallbackDto {

    private String fromId;

    private FriendDto toItem;
}
