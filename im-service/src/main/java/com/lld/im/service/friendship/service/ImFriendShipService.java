package com.lld.im.service.friendship.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.common.model.RequestBase;
import com.lld.im.common.model.SyncReq;
import com.lld.im.service.friendship.model.req.*;

import java.util.List;

public interface ImFriendShipService {

    ResponseVO importFriendShip(ImporFriendShipReq req);

    ResponseVO addFriend(AddFriendReq req);

    ResponseVO updateFriend(UpdateFriendReq req);

    ResponseVO deleteFriend(DeleteFriendReq req);

    ResponseVO deleteAllFriend(DeleteFriendReq req);

    ResponseVO getAllFriendShip(GetAllFriendShipReq req);

    ResponseVO getRelation(GetRelationReq req);

    ResponseVO checkFriendship(CheckFriendShipReq req);

    ResponseVO addBlack(AddFriendShipBlackReq req);

    ResponseVO deleteBlack(DeleteBlackReq req);

    ResponseVO checkBlck(CheckFriendShipReq req);

    ResponseVO doAddFriend(RequestBase requestBase, String fromId, FriendDto dto, Integer appId);

    ResponseVO syncFriendshipList(SyncReq req);

    List<String> getAllFriendId(String userId, Integer appId);
}
