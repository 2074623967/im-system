package com.lld.im.service.friendship.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.friendship.model.req.ApproverFriendRequestReq;
import com.lld.im.service.friendship.model.req.FriendDto;

public interface ImFriendShipRequestService {
    ResponseVO addFienshipRequest(String fromId, FriendDto dto, Integer appId);

    ResponseVO approverFriendRequest(ApproverFriendRequestReq req);
}
