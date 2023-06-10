package com.lld.im.service.user.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/06/10 17:19
 **/
@Data
public class PullUserOnlineStatusReq extends RequestBase {

    private List<String> userList;
}
