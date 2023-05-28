package com.lld.im.service.group.model.resp;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/05/28 10:43
 **/
@Data
public class GetRoleInGroupResp {

    private Long groupMemberId;

    private String memberId;

    private Integer role;

    private Long speakDate;
}
