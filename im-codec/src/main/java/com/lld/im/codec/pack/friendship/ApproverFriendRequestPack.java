package com.lld.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 20:40
 **/
@Data
public class ApproverFriendRequestPack {

    private Long id;

    //1同意 2拒绝
    private Integer status;

    private Long sequence;
}
