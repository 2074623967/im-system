package com.lld.im.tcp.publish;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.lld.im.codec.proto.Message;
import com.lld.im.common.constant.Constants;
import com.lld.im.common.enums.command.CommandType;
import com.lld.im.tcp.utils.MqFactory;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tangcj
 * @date 2023/05/31 11:59
 **/
@Slf4j
public class MqMessageProducer {

    public static void sendMessage(Message message, Integer command) {
        Channel channel;
        String com = command.toString();
        String commandSub = com.substring(0, 1);
        CommandType commandType = CommandType.getCommandType(commandSub);
        String channelName = "";
        if (commandType == CommandType.MESSAGE) {
            channelName = Constants.RabbitConstants.Im2MessageService;
        } else if (commandType == CommandType.GROUP) {
            channelName = Constants.RabbitConstants.Im2GroupService;
        }
        try {
            channel = MqFactory.getChannel(channelName);
            JSONObject pack = (JSONObject) JSON.toJSON(message.getMessagePack());
            pack.put("command", command);
            pack.put("clientType", message.getMessageHeader().getClientType());
            pack.put("imei", message.getMessageHeader().getImei());
            pack.put("appId", message.getMessageHeader().getAppId());
            channel.basicPublish(channelName, "", null,
                    pack.toString().getBytes());
        } catch (Exception e) {
            log.error("发送消息出现异常：{}", e.getMessage());
        }
    }
}
