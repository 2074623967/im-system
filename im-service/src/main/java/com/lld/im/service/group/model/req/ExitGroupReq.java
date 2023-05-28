package com.lld.im.service.group.model.req;

import com.lld.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tangcj
 * @date 2023/05/28 13:36
 **/
@Data
public class ExitGroupReq extends RequestBase {

    @NotBlank(message = "群id不能为空")
    private String groupId;
}
