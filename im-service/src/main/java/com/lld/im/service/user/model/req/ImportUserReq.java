package com.lld.im.service.user.model.req;

import com.lld.im.common.model.RequestBase;
import com.lld.im.service.user.dao.ImUserDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/25 21:41
 **/
@Data
public class ImportUserReq extends RequestBase {

    private List<ImUserDataEntity> userData;
}
