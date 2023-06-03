package com.lld.im.codec.pack.user;

import lombok.Data;

/**
 * @author tangcj
 * @date 2023/06/03 19:57
 **/
@Data
public class UserModifyPack {

    // 用户id
    private String userId;

    // 用户名称
    private String nickName;

    private String password;

    // 头像
    private String photo;

    // 性别
    private String userSex;

    // 个性签名
    private String selfSignature;

    // 加好友验证类型（Friend_AllowType） 1需要验证
    private Integer friendAllowType;
}
