package com.lld.im.service.friendship.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tangcj
 * @date 2023/05/27 17:55
 **/
@Data
public class AddFriendShipBlackReq extends RequestBase {

    @NotBlank(message = "用户id不能为空")
    private String fromId;

    private String toId;
}
