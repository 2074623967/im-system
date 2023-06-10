package com.lld.im.service.user.model;

import com.lld.im.common.model.ClientInfo;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/10 16:24
 **/
@Data
public class UserStatusChangeNotifyContent extends ClientInfo {


    private String userId;

    //服务端状态 1上线 2离线
    private Integer status;
}
