package com.lld.im.service.user.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/10 17:03
 **/
@Data
public class SetUserCustomerStatusReq extends RequestBase {

    private String userId;

    private String customText;

    private Integer customStatus;

}
