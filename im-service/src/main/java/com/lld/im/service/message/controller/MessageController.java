package com.lld.im.service.message.controller;

import com.lld.im.common.ResponseVO;
import com.lld.im.common.model.message.CheckSendMessageReq;
import com.lld.im.service.message.model.req.SendMessageReq;
import com.lld.im.service.message.service.P2PMessageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tangcj
 * @date 2023/06/04 13:33
 **/
@RestController
@RequestMapping("v1/message")
public class MessageController {

    @Resource
    private P2PMessageService p2PMessageService;

    @RequestMapping("/send")
    public ResponseVO send(@RequestBody @Validated SendMessageReq req, Integer appId)  {
        req.setAppId(appId);
        return ResponseVO.successResponse(p2PMessageService.send(req));
    }

    @RequestMapping("/checkSend")
    public ResponseVO checkSend(@RequestBody @Validated CheckSendMessageReq req)  {
        return p2PMessageService.imServerPermissionCheck(req.getFromId(),req.getToId(),req.getAppId());
    }
}
