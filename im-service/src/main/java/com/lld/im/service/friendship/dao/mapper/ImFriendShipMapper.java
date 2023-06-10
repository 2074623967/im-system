package com.lld.im.service.friendship.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lld.im.service.friendship.dao.ImFriendShipEntity;
import com.lld.im.service.friendship.model.req.CheckFriendShipReq;
import com.lld.im.service.friendship.model.resp.CheckFriendShipResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/27 12:00
 **/
public interface ImFriendShipMapper extends BaseMapper<ImFriendShipEntity> {


    List<CheckFriendShipResp> checkFriendShip(@Param("req") CheckFriendShipReq req);

    List<CheckFriendShipResp> checkFriendShipBoth(@Param("req")CheckFriendShipReq req);

    Long getFriendShipMaxSeq(@Param("appId") Integer appId, @Param("userId") String userId);
}
