package com.lld.im.codec.pack.group;

import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/06/03 21:15
 **/
@Data
public class AddGroupMemberPack {

    private String groupId;

    private List<String> members;
}
