package com.lld.im.service.friendship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lld.im.common.ResponseVO;
import com.lld.im.service.friendship.dao.ImFriendShipGroupEntity;
import com.lld.im.service.friendship.dao.ImFriendShipGroupMemberEntity;
import com.lld.im.service.friendship.dao.mapper.ImFriendShipGroupMemberMapper;
import com.lld.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.lld.im.service.friendship.model.req.DeleteFriendShipGroupMemberReq;
import com.lld.im.service.friendship.service.ImFriendShipGroupMemberService;
import com.lld.im.service.friendship.service.ImFriendShipGroupService;
import com.lld.im.service.user.dao.ImUserDataEntity;
import com.lld.im.service.user.service.ImUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/27 21:14
 **/
@Service
public class ImFriendShipGroupMemberServiceImpl implements ImFriendShipGroupMemberService {

    @Resource
    private ImFriendShipGroupMemberMapper imFriendShipGroupMemberMapper;

    @Resource
    private ImFriendShipGroupService imFriendShipGroupService;

    @Resource
    private ImUserService imUserService;

    @Resource
    private ImFriendShipGroupMemberService imFriendShipGroupMemberService;

    @Override
    @Transactional
    public ResponseVO addGroupMember(AddFriendShipGroupMemberReq req) {
        ResponseVO<ImFriendShipGroupEntity> group = imFriendShipGroupService
                .getGroup(req.getFromId(), req.getGroupName(), req.getAppId());
        if (!group.isOk()) {
            return group;
        }
        List<String> successId = new ArrayList<>();
        for (String toId : req.getToIds()) {
            ResponseVO<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(toId, req.getAppId());
            if (singleUserInfo.isOk()) {
                int i = imFriendShipGroupMemberService.doAddGroupMember(group.getData().getGroupId(), toId);
                if (i == 1) {
                    successId.add(toId);
                }
            }
        }
        return ResponseVO.successResponse(successId);
    }

    @Override
    public ResponseVO delGroupMember(DeleteFriendShipGroupMemberReq req) {
        ResponseVO<ImFriendShipGroupEntity> group = imFriendShipGroupService
                .getGroup(req.getFromId(), req.getGroupName(), req.getAppId());
        if (!group.isOk()) {
            return group;
        }
        List<String> successId = new ArrayList<>();
        for (String toId : req.getToIds()) {
            ResponseVO<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(toId, req.getAppId());
            if (singleUserInfo.isOk()) {
                int i = deleteGroupMember(group.getData().getGroupId(), toId);
                if (i == 1) {
                    successId.add(toId);
                }
            }
        }
        return ResponseVO.successResponse(successId);
    }

    @Override
    public int doAddGroupMember(Long groupId, String toId) {
        ImFriendShipGroupMemberEntity imFriendShipGroupMemberEntity = new ImFriendShipGroupMemberEntity();
        imFriendShipGroupMemberEntity.setGroupId(groupId);
        imFriendShipGroupMemberEntity.setToId(toId);
        try {
            int insert = imFriendShipGroupMemberMapper.insert(imFriendShipGroupMemberEntity);
            return insert;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteGroupMember(Long groupId, String toId) {
        QueryWrapper<ImFriendShipGroupMemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        queryWrapper.eq("to_id", toId);
        try {
            int delete = imFriendShipGroupMemberMapper.delete(queryWrapper);
//            int insert = imFriendShipGroupMemberMapper.insert(imFriendShipGroupMemberEntity);
            return delete;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int clearGroupMember(Long groupId) {
        QueryWrapper<ImFriendShipGroupMemberEntity> query = new QueryWrapper<>();
        query.eq("group_id", groupId);
        int delete = imFriendShipGroupMemberMapper.delete(query);
        return delete;
    }
}