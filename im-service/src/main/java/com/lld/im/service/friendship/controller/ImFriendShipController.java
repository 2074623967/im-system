package com.lld.im.service.friendship.controller;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.friendship.model.req.ImporFriendShipReq;
import com.lld.im.service.friendship.service.ImFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangcj
 * @date 2023/05/27 13:26
 **/
@RestController
@RequestMapping("v1/friendship")
public class ImFriendShipController {

    @Autowired
    ImFriendService imFriendShipService;

    @RequestMapping("/importFriendShip")
    public ResponseVO importFriendShip(@RequestBody @Validated ImporFriendShipReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.importFriendShip(req);
    }
}
