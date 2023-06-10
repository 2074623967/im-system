package com.lld.im.codec.pack.user;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/10 17:01
 **/
@Data
public class UserCustomStatusChangeNotifyPack {

    private String customText;

    private Integer customStatus;

    private String userId;
}
