package com.lld.im.service.friendship.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.friendship.model.req.AddFriendReq;
import com.lld.im.service.friendship.model.req.ImporFriendShipReq;
import com.lld.im.service.friendship.model.req.UpdateFriendReq;

public interface ImFriendShipService {

    ResponseVO importFriendShip(ImporFriendShipReq req);

    ResponseVO addFriend(AddFriendReq req);

    ResponseVO updateFriend(UpdateFriendReq req);
}
