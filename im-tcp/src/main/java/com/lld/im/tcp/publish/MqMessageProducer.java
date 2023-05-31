package com.lld.im.tcp.publish;

import com.alibaba.fastjson.JSONObject;
import com.lld.im.tcp.utils.MqFactory;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tangcj
 * @date 2023/05/31 11:59
 **/
@Slf4j
public class MqMessageProducer {

    public static void sendMessage(Object message) {
        Channel channel = null;
        String channelName = "";
        try {
            channel = MqFactory.getChannel(channelName);
            channel.basicPublish(channelName, "", null,
                    JSONObject.toJSONString(message).getBytes());
        } catch (Exception e) {
            log.error("发送消息出现异常：{}", e.getMessage());
        }
    }
}
