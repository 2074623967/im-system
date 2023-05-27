package com.lld.im.service.user.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/26 22:21
 **/
@Data
public class ImportUserResp {

    private List<String> successId;

    private List<String> errorId;
}
