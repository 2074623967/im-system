package com.lld.im.service.group.model.resp;

import com.lld.im.service.group.dao.ImGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/28 11:36
 **/
@Data
public class GetJoinedGroupResp {

    private Integer totalCount;

    private List<ImGroupEntity> groupList;
}
