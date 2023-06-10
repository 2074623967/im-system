package com.lld.im.service.group.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lld.im.service.group.dao.ImGroupMemberEntity;
import com.lld.im.service.group.model.req.GroupMemberDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImGroupMemberMapper extends BaseMapper<ImGroupMemberEntity> {

    List<GroupMemberDto> getGroupMember(@Param("appId") Integer appId, @Param("groupId") String groupId);

    List<String> getJoinedGroupId(@Param("appId") Integer appId, @Param("memberId") String memberId);

    List<String> getGroupMemberId(@Param("appId") Integer appId, @Param("groupId") String groupId);

    List<GroupMemberDto> getGroupManager(@Param("appId") String groupId, @Param("groupId") Integer appId);

    List<String> syncJoinedGroupId(@Param("appId")Integer appId,@Param("memberId") String memberId,@Param("role") int role);
}
