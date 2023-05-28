package com.lld.im.service.group.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.group.model.req.GroupMemberDto;
import com.lld.im.service.group.model.req.ImportGroupMemberReq;

public interface ImGroupMemberService {
    ResponseVO importGroupMember(ImportGroupMemberReq req);

    ResponseVO addGroupMember(String groupId, Integer appId, GroupMemberDto dto);
}
