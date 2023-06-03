package com.lld.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 20:32
 **/
@Data
public class DeleteBlackPack {

    private String fromId;

    private String toId;

    private Long sequence;
}
