package com.lld.im.service.friendship.service.impl;

import com.lld.im.common.ResponseVO;
import com.lld.im.service.friendship.dao.ImFriendShipEntity;
import com.lld.im.service.friendship.dao.mapper.ImFriendShipMapper;
import com.lld.im.service.friendship.model.req.ImporFriendShipReq;
import com.lld.im.service.friendship.model.resp.ImportFriendShipResp;
import com.lld.im.service.friendship.service.ImFriendService;
import com.lld.im.service.user.model.resp.ImportUserResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/27 12:02
 **/
@Service
public class ImFriendServiceImpl implements ImFriendService {

    @Resource
    private ImFriendShipMapper imFriendShipMapper;

    @Override
    public ResponseVO importFriendShip(ImporFriendShipReq req) {
        if (req.getFriendItem().size() > 100) {
            //todo 返回超出长度
        }
        ImportFriendShipResp resp = new ImportFriendShipResp();
        List<String> successId = new ArrayList<>();
        List<String> errorId = new ArrayList<>();
        for (ImporFriendShipReq.ImportFriendDto importFriendDto : req.getFriendItem()) {
            try {
                ImFriendShipEntity imFriendShipEntity = new ImFriendShipEntity();
                BeanUtils.copyProperties(importFriendDto, imFriendShipEntity);
                imFriendShipEntity.setAppId(req.getAppId());
                imFriendShipEntity.setFromId(req.getFromId());
                int insert = imFriendShipMapper.insert(imFriendShipEntity);
                if (insert == 1) {
                    successId.add(importFriendDto.getToId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorId.add(importFriendDto.getToId());
            }
        }
        resp.setErrorId(errorId);
        resp.setSuccessId(successId);
        return ResponseVO.successResponse(resp);
    }
}
