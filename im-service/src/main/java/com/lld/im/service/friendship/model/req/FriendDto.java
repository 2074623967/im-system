package com.lld.im.service.friendship.model.req;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/27 13:32
 **/
@Data
public class FriendDto {

    private String toId;

    private String remark;

    private String addSource;

    private String extra;

    private String addWording;
}
