package com.lld.im.service.user.service;

import com.lld.im.service.user.model.UserStatusChangeNotifyContent;

public interface ImUserStatusService {

    void processUserOnlineStatusNotify(UserStatusChangeNotifyContent content);
}
