package com.lld.im.common.router;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 11:11
 **/
@Data
public class RouteInfo {

    private String ip;

    private Integer port;

    public RouteInfo(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
}
