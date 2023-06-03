package com.lld.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 20:15
 **/
@Data
public class AddFriendPack {

    private String fromId;

    /**
     * 备注
     */
    private String remark;

    private String toId;

    /**
     * 好友来源
     */
    private String addSource;
    /**
     * 添加好友时的描述信息（用于打招呼）
     */
    private String addWording;

    private Long sequence;
}
