package com.lld.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 21:06
 **/
@Data
public class AddFriendGroupPack {

    public String fromId;

    private String groupName;

    /** 序列号*/
    private Long sequence;
}
