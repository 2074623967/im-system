package com.lld.im.codec.pack.friendship;

import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/06/03 21:08
 **/
@Data
public class AddFriendGroupMemberPack {

    public String fromId;

    private String groupName;

    private List<String> toIds;

    /** 序列号*/
    private Long sequence;
}
