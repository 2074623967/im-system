package com.lld.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 20:41
 **/
@Data
public class ReadAllFriendRequestPack {

    private String fromId;

    private Long sequence;
}
