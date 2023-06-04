package com.lld.im.service.message.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lld.im.common.constant.Constants;
import com.lld.im.common.enums.command.MessageCommand;
import com.lld.im.service.message.model.MessageContent;
import com.lld.im.service.message.service.P2PMessageService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author tangcj
 * @date 2023/06/04 09:33
 **/
@Component
public class ChatOperateReceiver {

    private static Logger logger = LoggerFactory.getLogger(ChatOperateReceiver.class);

    @Resource
    private P2PMessageService p2PMessageService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = Constants.RabbitConstants.Im2MessageService, durable = "true"),
                    exchange = @Exchange(value = Constants.RabbitConstants.Im2MessageService, durable = "true")
            ), concurrency = "1"
    )
    public void onChatMessage(@Payload Message message,
                              @Header Map<String, Object> headers,
                              Channel channel) throws Exception {
        String msg = new String(message.getBody(), "utf-8");
        logger.info("CHAT MSG FORM QUEUE ::: {}", msg);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            Integer command = jsonObject.getInteger("command");
            if (command.equals(MessageCommand.MSG_P2P.getCommand())) {
                //处理消息
                MessageContent messageContent = jsonObject.toJavaObject(MessageContent.class);
                p2PMessageService.process(messageContent);
            }
        } catch (Exception e) {
            logger.error("处理消息出现异常：{}", e.getMessage());
            logger.error("RMQ_CHAT_TRAN_ERROR", e);
            logger.error("NACK_MSG:{}", msg);
            //第一个false 表示不批量拒绝，第二个false表示不重回队列
            channel.basicNack(deliveryTag, false, false);
        }

    }
}
