package com.lld.im.service.group.controller;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.group.model.req.ImportGroupMemberReq;
import com.lld.im.service.group.service.ImGroupMemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tangcj
 * @date 2023/05/28 09:26
 **/
@RestController
@RequestMapping("v1/group/member")
public class ImGroupMemberController {

    @Resource
    private ImGroupMemberService imGroupMemberService;

    @RequestMapping("/importGroupMember")
    public ResponseVO importGroupMember(@RequestBody @Validated ImportGroupMemberReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperater(identifier);
        return imGroupMemberService.importGroupMember(req);
    }
}
