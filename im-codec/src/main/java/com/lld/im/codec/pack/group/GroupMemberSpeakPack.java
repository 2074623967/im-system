package com.lld.im.codec.pack.group;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 21:49
 **/
@Data
public class GroupMemberSpeakPack {

    private String groupId;

    private String memberId;

    private Long speakDate;
}
