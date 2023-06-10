package com.lld.im.service.group.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lld.im.codec.pack.group.CreateGroupPack;
import com.lld.im.codec.pack.group.DestroyGroupPack;
import com.lld.im.codec.pack.group.UpdateGroupInfoPack;
import com.lld.im.common.ResponseVO;
import com.lld.im.common.config.AppConfig;
import com.lld.im.common.constant.Constants;
import com.lld.im.common.enums.GroupErrorCode;
import com.lld.im.common.enums.GroupMemberRoleEnum;
import com.lld.im.common.enums.GroupStatusEnum;
import com.lld.im.common.enums.GroupTypeEnum;
import com.lld.im.common.enums.command.GroupEventCommand;
import com.lld.im.common.exception.ApplicationException;
import com.lld.im.common.model.ClientInfo;
import com.lld.im.service.group.dao.ImGroupEntity;
import com.lld.im.service.group.dao.mapper.ImGroupMapper;
import com.lld.im.service.group.model.callback.DestroyGroupCallbackDto;
import com.lld.im.service.group.model.req.*;
import com.lld.im.service.group.model.resp.GetGroupResp;
import com.lld.im.service.group.model.resp.GetJoinedGroupResp;
import com.lld.im.service.group.model.resp.GetRoleInGroupResp;
import com.lld.im.service.group.service.ImGroupMemberService;
import com.lld.im.service.group.service.ImGroupService;
import com.lld.im.service.seq.RedisSeq;
import com.lld.im.service.utils.CallbackService;
import com.lld.im.service.utils.GroupMessageProducer;
import com.lld.im.service.utils.MessageProducer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author tangcj
 * @date 2023/05/28 09:17
 **/
@Service
public class ImGroupServiceImpl implements ImGroupService {

    @Resource
    private ImGroupMapper imGroupMapper;

    @Resource
    private ImGroupMemberService imGroupMemberService;

    @Resource
    private ImGroupService imGroupService;

    @Resource
    private AppConfig appConfig;

    @Resource
    private CallbackService callbackService;

    @Resource
    private GroupMessageProducer groupMessageProducer;

    @Resource
    private RedisSeq redisSeq;

    @Override
    public ResponseVO importGroup(ImportGroupReq req) {
        //1.判断群id是否存在
        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        if (StringUtils.isEmpty(req.getGroupId())) {
            req.setGroupId(UUID.randomUUID().toString().replace("-", ""));
        } else {
            query.eq("group_id", req.getGroupId());
            query.eq("app_id", req.getAppId());
            Integer integer = imGroupMapper.selectCount(query);
            if (integer > 0) {
                throw new ApplicationException(GroupErrorCode.GROUP_IS_EXIST);
            }
        }
        ImGroupEntity imGroupEntity = new ImGroupEntity();
        if (req.getGroupType() == GroupTypeEnum.PUBLIC.getCode() && StringUtils.isBlank(req.getOwnerId())) {
            throw new ApplicationException(GroupErrorCode.PUBLIC_GROUP_MUST_HAVE_OWNER);
        }
        if (req.getCreateTime() == null) {
            imGroupEntity.setCreateTime(System.currentTimeMillis());
        }
        imGroupEntity.setStatus(GroupStatusEnum.NORMAL.getCode());
        BeanUtils.copyProperties(req, imGroupEntity);
        int insert = imGroupMapper.insert(imGroupEntity);
        if (insert != 1) {
            throw new ApplicationException(GroupErrorCode.IMPORT_GROUP_ERROR);
        }
        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO<ImGroupEntity> getGroup(String groupId, Integer appId) {
        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        query.eq("app_id", appId);
        query.eq("group_id", groupId);
        ImGroupEntity imGroupEntity = imGroupMapper.selectOne(query);
        if (imGroupEntity == null) {
            return ResponseVO.errorResponse(GroupErrorCode.GROUP_IS_NOT_EXIST);
        }
        return ResponseVO.successResponse(imGroupEntity);
    }

    /**
     * @param req
     * @return com.lld.im.common.ResponseVO
     * 修改群基础信息，如果是后台管理员调用，则不检查权限，如果不是则检查权限，如果是私有群（微信群）任何人都可以修改资料，公开群只有管理员可以修改
     * 如果是群主或者管理员可以修改其他信息。
     **/
    @Override
    @Transactional
    public ResponseVO updateBaseGroupInfo(UpdateGroupReq req) {
        //1.判断群id是否存在
        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        query.eq("group_id", req.getGroupId());
        query.eq("app_id", req.getAppId());
        ImGroupEntity imGroupEntity = imGroupMapper.selectOne(query);
        if (imGroupEntity == null) {
            throw new ApplicationException(GroupErrorCode.GROUP_IS_EXIST);
        }
        if (imGroupEntity.getStatus() == GroupStatusEnum.DESTROY.getCode()) {
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }
        boolean isAdmin = false;
        if (!isAdmin) {
            //不是后台调用需要检查权限
            ResponseVO<GetRoleInGroupResp> role = imGroupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOperater(), req.getAppId());
            if (!role.isOk()) {
                return role;
            }
            GetRoleInGroupResp data = role.getData();
            Integer roleInfo = data.getRole();
            boolean isManager = roleInfo == GroupMemberRoleEnum.MAMAGER.getCode() || roleInfo == GroupMemberRoleEnum.OWNER.getCode();
            //公开群只能群主修改资料
            if (!isManager && GroupTypeEnum.PUBLIC.getCode() == imGroupEntity.getGroupType()) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
            }
        }
        long seq = redisSeq.doGetSeq(req.getAppId() + ":" + Constants.SeqConstants.Group);
        ImGroupEntity update = new ImGroupEntity();
        BeanUtils.copyProperties(req, update);
        update.setSequence(seq);
        update.setUpdateTime(System.currentTimeMillis());
        int row = imGroupMapper.update(update, query);
        if (row != 1) {
            throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
        }
        if (appConfig.isModifyGroupAfterCallback()) {
            callbackService.callback(req.getAppId(), Constants.CallbackCommand.UpdateGroupAfter,
                    JSONObject.toJSONString(imGroupMapper.selectOne(query)));
        }
        UpdateGroupInfoPack pack = new UpdateGroupInfoPack();
        BeanUtils.copyProperties(req, pack);
        groupMessageProducer.producer(req.getOperater(), GroupEventCommand.UPDATED_GROUP, pack,
                new ClientInfo(req.getAppId(), req.getClientType(), req.getImei()));
        return ResponseVO.successResponse();
    }

    @Override
    @Transactional
    public ResponseVO createGroup(CreateGroupReq req) {
        boolean isAdmin = false;
        if (!isAdmin) {
            req.setOwnerId(req.getOperater());
        }
        //1.判断群id是否存在
        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        if (StringUtils.isEmpty(req.getGroupId())) {
            req.setGroupId(UUID.randomUUID().toString().replace("-", ""));
        } else {
            query.eq("group_id", req.getGroupId());
            query.eq("app_id", req.getAppId());
            Integer integer = imGroupMapper.selectCount(query);
            if (integer > 0) {
                throw new ApplicationException(GroupErrorCode.GROUP_IS_EXIST);
            }
        }
        if (req.getGroupType() == GroupTypeEnum.PUBLIC.getCode() && StringUtils.isBlank(req.getOwnerId())) {
            throw new ApplicationException(GroupErrorCode.PUBLIC_GROUP_MUST_HAVE_OWNER);
        }
        long seq = redisSeq.doGetSeq(req.getAppId() + ":" + Constants.SeqConstants.Group);
        ImGroupEntity imGroupEntity = new ImGroupEntity();
        imGroupEntity.setSequence(seq);
        imGroupEntity.setCreateTime(System.currentTimeMillis());
        imGroupEntity.setStatus(GroupStatusEnum.NORMAL.getCode());
        BeanUtils.copyProperties(req, imGroupEntity);
        int insert = imGroupMapper.insert(imGroupEntity);
        GroupMemberDto groupMemberDto = new GroupMemberDto();
        groupMemberDto.setMemberId(req.getOwnerId());
        groupMemberDto.setRole(GroupMemberRoleEnum.OWNER.getCode());
        groupMemberDto.setJoinTime(System.currentTimeMillis());
        imGroupMemberService.addGroupMember(req.getGroupId(), req.getAppId(), groupMemberDto);
        //插入群成员
        for (GroupMemberDto dto : req.getMember()) {
            imGroupMemberService.addGroupMember(req.getGroupId(), req.getAppId(), dto);
        }
        if (appConfig.isCreateGroupAfterCallback()) {
            callbackService.callback(req.getAppId(), Constants.CallbackCommand.CreateGroupAfter,
                    JSONObject.toJSONString(imGroupEntity));
        }
        CreateGroupPack createGroupPack = new CreateGroupPack();
        BeanUtils.copyProperties(imGroupEntity, createGroupPack);
        groupMessageProducer.producer(req.getOperater(), GroupEventCommand.CREATED_GROUP, createGroupPack,
                new ClientInfo(req.getAppId(), req.getClientType(), req.getImei()));
        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO getGroup(GetGroupReq req) {
        ResponseVO group = this.getGroup(req.getGroupId(), req.getAppId());
        if (!group.isOk()) {
            return group;
        }
        GetGroupResp getGroupResp = new GetGroupResp();
        BeanUtils.copyProperties(group.getData(), getGroupResp);
        try {
            ResponseVO<List<GroupMemberDto>> groupMember = imGroupMemberService.getGroupMember(req.getGroupId(), req.getAppId());
            if (groupMember.isOk()) {
                getGroupResp.setMemberList(groupMember.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseVO.successResponse(getGroupResp);
    }

    /**
     * @param req
     * @return com.lld.im.common.ResponseVO
     * 获取用户加入的群组
     **/
    @Override
    public ResponseVO getJoinedGroup(GetJoinedGroupReq req) {
        //获取所有加入的群id
        ResponseVO<Collection<String>> memberJoinedGroup = imGroupMemberService.getMemberJoinedGroup(req);
        if (memberJoinedGroup.isOk()) {
            GetJoinedGroupResp resp = new GetJoinedGroupResp();
            if (CollectionUtils.isEmpty(memberJoinedGroup.getData())) {
                resp.setTotalCount(0);
                resp.setGroupList(new ArrayList<>());
                return ResponseVO.successResponse(resp);
            }
            QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
            query.eq("app_id", req.getAppId());
            query.in("group_id", memberJoinedGroup.getData());
            if (CollectionUtils.isNotEmpty(req.getGroupType())) {
                query.in("group_type", req.getGroupType());
            }
            //根据群id获取群信息
            List<ImGroupEntity> groupList = imGroupMapper.selectList(query);
            resp.setGroupList(groupList);
            if (req.getLimit() == null) {
                resp.setTotalCount(groupList.size());
            } else {
                resp.setTotalCount(imGroupMapper.selectCount(query));
            }
            return ResponseVO.successResponse(resp);
        } else {
            return memberJoinedGroup;
        }
    }

    /**
     * @param req
     * @return com.lld.im.common.ResponseVO
     * 解散群组，只支持后台管理员和群主解散
     **/
    @Override
    @Transactional
    public ResponseVO destroyGroup(DestroyGroupReq req) {
        boolean isAdmin = false;
        QueryWrapper<ImGroupEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("group_id", req.getGroupId());
        objectQueryWrapper.eq("app_id", req.getAppId());
        ImGroupEntity imGroupEntity = imGroupMapper.selectOne(objectQueryWrapper);
        if (imGroupEntity == null) {
            throw new ApplicationException(GroupErrorCode.PRIVATE_GROUP_CAN_NOT_DESTORY);
        }
        if (imGroupEntity.getStatus() == GroupStatusEnum.DESTROY.getCode()) {
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }
        if (!isAdmin) {
            if (imGroupEntity.getGroupType() == GroupTypeEnum.PUBLIC.getCode()) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
            }
            if (imGroupEntity.getGroupType() == GroupTypeEnum.PUBLIC.getCode() &&
                    !imGroupEntity.getOwnerId().equals(req.getOperater())) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
            }
        }
        long seq = redisSeq.doGetSeq(req.getAppId() + ":" + Constants.SeqConstants.Group);
        ImGroupEntity update = new ImGroupEntity();
        update.setSequence(seq);
        update.setStatus(GroupStatusEnum.DESTROY.getCode());
        int update1 = imGroupMapper.update(update, objectQueryWrapper);
        if (update1 != 1) {
            throw new ApplicationException(GroupErrorCode.UPDATE_GROUP_BASE_INFO_ERROR);
        }
        if (appConfig.isModifyGroupAfterCallback()) {
            DestroyGroupCallbackDto dto = new DestroyGroupCallbackDto();
            dto.setGroupId(req.getGroupId());
            callbackService.callback(req.getAppId()
                    , Constants.CallbackCommand.DestoryGroupAfter,
                    JSONObject.toJSONString(dto));
        }
        DestroyGroupPack pack = new DestroyGroupPack();
        pack.setSequence(seq);
        pack.setGroupId(req.getGroupId());
        groupMessageProducer.producer(req.getOperater(), GroupEventCommand.DESTROY_GROUP, pack,
                new ClientInfo(req.getAppId(), req.getClientType(), req.getImei()));
        return ResponseVO.successResponse();
    }

    @Override
    @Transactional
    public ResponseVO transferGroup(TransferGroupReq req) {
        ResponseVO<GetRoleInGroupResp> roleInGroupOne = imGroupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOperater(), req.getAppId());
        if (!roleInGroupOne.isOk()) {
            return roleInGroupOne;
        }
        if (roleInGroupOne.getData().getRole() != GroupMemberRoleEnum.OWNER.getCode()) {
            return ResponseVO.errorResponse(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
        }
        ResponseVO<GetRoleInGroupResp> newOwnerRole = imGroupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOwnerId(), req.getAppId());
        if (!newOwnerRole.isOk()) {
            return newOwnerRole;
        }
        QueryWrapper<ImGroupEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("group_id", req.getGroupId());
        objectQueryWrapper.eq("app_id", req.getAppId());
        ImGroupEntity imGroupEntity = imGroupMapper.selectOne(objectQueryWrapper);
        if (imGroupEntity.getStatus() == GroupStatusEnum.DESTROY.getCode()) {
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }
        long seq = redisSeq.doGetSeq(req.getAppId() + ":" + Constants.SeqConstants.Group);
        ImGroupEntity updateGroup = new ImGroupEntity();
        updateGroup.setSequence(seq);
        updateGroup.setOwnerId(req.getOwnerId());
        UpdateWrapper<ImGroupEntity> updateGroupWrapper = new UpdateWrapper<>();
        updateGroupWrapper.eq("app_id", req.getAppId());
        updateGroupWrapper.eq("group_id", req.getGroupId());
        imGroupMapper.update(updateGroup, updateGroupWrapper);
        imGroupMemberService.transferGroupMember(req.getOwnerId(), req.getGroupId(), req.getAppId());
        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO muteGroup(MuteGroupReq req) {
        ResponseVO<ImGroupEntity> groupResp = getGroup(req.getGroupId(), req.getAppId());
        if (!groupResp.isOk()) {
            return groupResp;
        }
        if (groupResp.getData().getStatus() == GroupStatusEnum.DESTROY.getCode()) {
            throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);
        }
        boolean isadmin = false;
        if (!isadmin) {
            //不是后台调用需要检查权限
            ResponseVO<GetRoleInGroupResp> role = imGroupMemberService.getRoleInGroupOne(req.getGroupId(), req.getOperater(), req.getAppId());
            if (!role.isOk()) {
                return role;
            }
            GetRoleInGroupResp data = role.getData();
            Integer roleInfo = data.getRole();
            boolean isManager = roleInfo == GroupMemberRoleEnum.MAMAGER.getCode() || roleInfo == GroupMemberRoleEnum.OWNER.getCode();
            //公开群只能群主修改资料
            if (!isManager) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_MANAGER_ROLE);
            }
        }
        ImGroupEntity update = new ImGroupEntity();
        update.setMute(req.getMute());
        UpdateWrapper<ImGroupEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("group_id", req.getGroupId());
        wrapper.eq("app_id", req.getAppId());
        imGroupMapper.update(update, wrapper);
        return ResponseVO.successResponse();
    }
}
