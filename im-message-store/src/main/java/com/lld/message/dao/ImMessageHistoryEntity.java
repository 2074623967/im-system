package com.lld.message.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/04 12:47
 **/
@Data
@TableName("im_message_history")
public class ImMessageHistoryEntity {

    private Integer appId;

    private String fromId;

    private String toId;

    private String ownerId;

    /** messageBodyId*/
    private Long messageKey;
    /** 序列号*/
    private Long sequence;

    private String messageRandom;

    private Long messageTime;

    private Long createTime;
}
