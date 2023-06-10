package com.lld.im.service.user.service;

import com.lld.im.service.user.model.UserStatusChangeNotifyContent;
import com.lld.im.service.user.model.req.SetUserCustomerStatusReq;
import com.lld.im.service.user.model.req.SubscribeUserOnlineStatusReq;

public interface ImUserStatusService {

    void processUserOnlineStatusNotify(UserStatusChangeNotifyContent content);

    void subscribeUserOnlineStatus(SubscribeUserOnlineStatusReq req);

    void setUserCustomerStatus(SetUserCustomerStatusReq req);
}
