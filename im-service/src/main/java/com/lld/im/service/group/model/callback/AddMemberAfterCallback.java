package com.lld.im.service.group.model.callback;

import com.lld.im.service.group.model.resp.AddMemberResp;
import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/06/03 16:22
 **/
@Data
public class AddMemberAfterCallback {

    private String groupId;

    private Integer groupType;

    private String operater;

    private List<AddMemberResp> memberId;
}
