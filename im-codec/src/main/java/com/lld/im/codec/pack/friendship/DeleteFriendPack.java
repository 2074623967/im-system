package com.lld.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 20:24
 **/
@Data
public class DeleteFriendPack {

    private String fromId;

    private String toId;

    private Long sequence;
}
