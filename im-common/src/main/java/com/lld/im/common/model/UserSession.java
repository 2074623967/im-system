package com.lld.im.common.model;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/30 21:22
 **/
@Data
public class UserSession {

    private String userId;

    /**
     * 应用ID
     */
    private Integer appId;

    /**
     * 端的标识
     */
    private Integer clientType;

    /**
     * sdk 版本号
     */
    private Integer version;

    /**
     * 连接状态 1=在线 2=离线
     */
    private Integer connectState;

    private Integer brokerId;

    private String brokerHost;

    private String imei;
}
