package com.lld.im.common.model.message;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 15:42
 **/
@Data
public class ImMessageBody {
    private Integer appId;

    /** messageBodyId*/
    private Long messageKey;

    /** messageBody*/
    private String messageBody;

    private String securityKey;

    private Long messageTime;

    private Long createTime;

    private String extra;

    private Integer delFlag;
}
