package com.lld.im.common.model;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/10 11:53
 **/
@Data
public class SyncReq extends RequestBase {

    //客户端最大seq
    private Long lastSequence;

    //一次拉取多少
    private Integer maxLimit;
}