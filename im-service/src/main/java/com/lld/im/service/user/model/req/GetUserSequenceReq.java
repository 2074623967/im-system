package com.lld.im.service.user.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/26 22:37
 **/
@Data
public class GetUserSequenceReq extends RequestBase {

    private String userId;

}
