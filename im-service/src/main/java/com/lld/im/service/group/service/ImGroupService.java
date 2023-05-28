package com.lld.im.service.group.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.group.dao.ImGroupEntity;
import com.lld.im.service.group.model.req.ImportGroupReq;

public interface ImGroupService {
    ResponseVO importGroup(ImportGroupReq req);

    ResponseVO<ImGroupEntity> getGroup(String groupId, Integer appId);
}
