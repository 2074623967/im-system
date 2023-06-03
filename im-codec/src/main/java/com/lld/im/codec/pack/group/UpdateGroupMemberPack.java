package com.lld.im.codec.pack.group;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 21:16
 **/
@Data
public class UpdateGroupMemberPack {
    private String groupId;

    private String memberId;

    private String alias;

    private String extra;
}
