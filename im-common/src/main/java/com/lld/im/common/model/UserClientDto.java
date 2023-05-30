package com.lld.im.common.model;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/30 21:45
 **/
@Data
public class UserClientDto {

    private Integer appId;

    private Integer clientType;

    private String userId;

    private String imei;
}
