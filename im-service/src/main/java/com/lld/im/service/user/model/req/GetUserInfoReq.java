package com.lld.im.service.user.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/26 22:34
 **/
@Data
public class GetUserInfoReq extends RequestBase {

    private List<String> userIds;
}
