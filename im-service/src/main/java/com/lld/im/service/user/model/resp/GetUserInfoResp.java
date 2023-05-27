package com.lld.im.service.user.model.resp;

import com.lld.im.service.user.dao.ImUserDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/26 22:34
 **/
@Data
public class GetUserInfoResp {

    private List<ImUserDataEntity> userDataItem;

    private List<String> failUser;
}
