package com.lld.im.common.constant;

/**
 * @author tangcj
 * @date 2023/05/30 21:34
 **/
public class Constants {

    public static class RedisConstants {

        /**
         * userSign，格式：appId:userSign:
         */
        public static final String userSign = "userSign";

        /**
         * 用户上线通知channel
         */
        public static final String UserLoginChannel = "signal/channel/LOGIN_USER_INNER_QUEUE";

        /**
         * 用户session，appId + UserSessionConstants + 用户id 例如10000：userSession：lld
         */
        public static final String UserSessionConstants = ":userSession:";

        /**
         * 缓存客户端消息防重，格式： appId + :cacheMessage: + messageId
         */
        public static final String cacheMessage = "cacheMessage";

        public static final String OfflineMessage = "offlineMessage";

        /**
         * seq 前缀
         */
        public static final String SeqPrefix = "seq";

        /**
         * 用户订阅列表，格式 ：appId + :subscribe: + userId。Hash结构，filed为订阅自己的人
         */
        public static final String subscribe = "subscribe";

        /**
         * 用户自定义在线状态，格式 ：appId + :userCustomerStatus: + userId。set，value为用户id
         */
        public static final String userCustomerStatus = "userCustomerStatus";

    }
}
