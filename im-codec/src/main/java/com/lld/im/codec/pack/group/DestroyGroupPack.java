package com.lld.im.codec.pack.group;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 21:38
 **/
@Data
public class DestroyGroupPack {

    private String groupId;

    private Long sequence;
}
