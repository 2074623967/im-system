package com.lld.im.tcp.reciver;

import com.lld.im.codec.proto.MessagePack;
import com.lld.im.common.constant.Constants;
import com.lld.im.tcp.utils.MqFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author tangcj
 * @date 2023/05/31 12:03
 **/
@Slf4j
public class MessageReciver {

    private static void startReciverMessage() {
        try {
            Channel channel = MqFactory
                    .getChannel(Constants.RabbitConstants.MessageService2Im);
            channel.queueDeclare(Constants.RabbitConstants.MessageService2Im,
                    true, false, false, null
            );
            channel.queueBind(Constants.RabbitConstants.MessageService2Im,
                    Constants.RabbitConstants.MessageService2Im, "");
            channel.basicConsume(Constants.RabbitConstants
                            .MessageService2Im, false,
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            try {
                                String msgStr = new String(body);
                                log.info(msgStr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        startReciverMessage();
    }
}
