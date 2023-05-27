package com.lld.im.common.model;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/25 21:42
 **/
@Data
public class RequestBase {

    private Integer appId;

    private String operater;

    private Integer clientType;

    private String imei;
}
