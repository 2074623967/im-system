package com.lld.im.service.group.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.group.model.req.GroupMemberDto;
import com.lld.im.service.group.model.req.ImportGroupMemberReq;
import com.lld.im.service.group.model.resp.GetRoleInGroupResp;

import java.util.List;

public interface ImGroupMemberService {

    ResponseVO importGroupMember(ImportGroupMemberReq req);

    ResponseVO addGroupMember(String groupId, Integer appId, GroupMemberDto dto);

    ResponseVO<GetRoleInGroupResp> getRoleInGroupOne(String groupId, String memberId, Integer appId);

    ResponseVO<List<GroupMemberDto>> getGroupMember(String groupId, Integer appId);
}
