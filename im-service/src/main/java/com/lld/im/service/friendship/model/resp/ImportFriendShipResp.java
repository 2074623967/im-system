package com.lld.im.service.friendship.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/27 13:24
 **/
@Data
public class ImportFriendShipResp {

    private List<String> successId;

    private List<String> errorId;
}
