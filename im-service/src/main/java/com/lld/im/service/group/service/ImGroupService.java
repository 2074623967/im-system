package com.lld.im.service.group.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.common.model.SyncReq;
import com.lld.im.service.group.dao.ImGroupEntity;
import com.lld.im.service.group.model.req.*;

public interface ImGroupService {

    ResponseVO importGroup(ImportGroupReq req);

    ResponseVO<ImGroupEntity> getGroup(String groupId, Integer appId);

    ResponseVO updateBaseGroupInfo(UpdateGroupReq req);

    ResponseVO createGroup(CreateGroupReq req);

    ResponseVO getGroup(GetGroupReq req);

    ResponseVO getJoinedGroup(GetJoinedGroupReq req);

    ResponseVO destroyGroup(DestroyGroupReq req);

    ResponseVO transferGroup(TransferGroupReq req);

    ResponseVO muteGroup(MuteGroupReq req);

    ResponseVO syncJoinedGroupList(SyncReq req);

    Long getUserGroupMaxSeq(String userId, Integer appId);
}
