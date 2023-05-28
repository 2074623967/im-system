package com.lld.im.service.group.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.group.dao.ImGroupEntity;
import com.lld.im.service.group.model.req.CreateGroupReq;
import com.lld.im.service.group.model.req.GetGroupReq;
import com.lld.im.service.group.model.req.ImportGroupReq;
import com.lld.im.service.group.model.req.UpdateGroupReq;

public interface ImGroupService {

    ResponseVO importGroup(ImportGroupReq req);

    ResponseVO<ImGroupEntity> getGroup(String groupId, Integer appId);

    ResponseVO updateBaseGroupInfo(UpdateGroupReq req);

    ResponseVO createGroup(CreateGroupReq req);

    ResponseVO getGroup(GetGroupReq req);
}
