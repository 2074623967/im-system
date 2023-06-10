package com.lld.im.service.group.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.group.model.req.*;
import com.lld.im.service.group.model.resp.GetRoleInGroupResp;

import java.util.Collection;
import java.util.List;

public interface ImGroupMemberService {

    ResponseVO importGroupMember(ImportGroupMemberReq req);

    ResponseVO addGroupMember(String groupId, Integer appId, GroupMemberDto dto);

    ResponseVO<GetRoleInGroupResp> getRoleInGroupOne(String groupId, String memberId, Integer appId);

    ResponseVO<List<GroupMemberDto>> getGroupMember(String groupId, Integer appId);

    ResponseVO<Collection<String>> getMemberJoinedGroup(GetJoinedGroupReq req);

    ResponseVO transferGroupMember(String ownerId, String groupId, Integer appId);

    ResponseVO addMember(AddGroupMemberReq req);

    ResponseVO removeMember(RemoveGroupMemberReq req);

    ResponseVO removeGroupMember(String groupId, Integer appId, String memberId);

    ResponseVO exitGroup(ExitGroupReq req);

    ResponseVO updateGroupMember(UpdateGroupMemberReq req);

    ResponseVO speak(SpeaMemberReq req);

    List<String> getGroupMemberId(String groupId, Integer appId);

    List<GroupMemberDto> getGroupManager(String groupId, Integer appId);

    ResponseVO<Collection<String>> syncMemberJoinedGroup(String operater, Integer appId);
}
