package com.lld.im.service.group.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/28 11:05
 **/
@Data
public class GetGroupReq extends RequestBase {

    private String groupId;
}
