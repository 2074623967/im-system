package com.lld.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 20:23
 **/
@Data
public class UpdateFriendPack {

    public String fromId;

    private String toId;

    private String remark;

    private Long sequence;
}
