package com.lld.im.service.friendship.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.friendship.model.req.ImporFriendShipReq;

public interface ImFriendService {

    public ResponseVO importFriendShip(ImporFriendShipReq req);
}
