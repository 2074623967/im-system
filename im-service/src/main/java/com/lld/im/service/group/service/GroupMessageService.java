package com.lld.im.service.group.service;

import com.lld.im.codec.pack.message.ChatMessageAck;
import com.lld.im.common.ResponseVO;
import com.lld.im.common.constant.Constants;
import com.lld.im.common.enums.command.GroupEventCommand;
import com.lld.im.common.model.ClientInfo;
import com.lld.im.common.model.message.GroupChatMessageContent;
import com.lld.im.common.model.message.OfflineMessageContent;
import com.lld.im.service.group.model.req.SendGroupMessageReq;
import com.lld.im.service.message.model.resp.SendMessageResp;
import com.lld.im.service.message.service.CheckSendMessageService;
import com.lld.im.service.message.service.MessageStoreService;
import com.lld.im.service.message.service.P2PMessageService;
import com.lld.im.service.seq.RedisSeq;
import com.lld.im.service.utils.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangcj
 * @date 2023/06/04 11:34
 **/
@Service
public class GroupMessageService {

    private static Logger logger = LoggerFactory.getLogger(P2PMessageService.class);

    @Resource
    private CheckSendMessageService checkSendMessageService;

    @Resource
    private MessageProducer messageProducer;

    @Resource
    private ImGroupMemberService imGroupMemberService;

    @Resource
    private MessageStoreService messageStoreService;

    @Resource
    private RedisSeq redisSeq;

    private final ThreadPoolExecutor threadPoolExecutor;

    {
        AtomicInteger num = new AtomicInteger(0);
        threadPoolExecutor = new ThreadPoolExecutor(8, 8, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("message-group-thread-" + num.getAndIncrement());
                return thread;
            }
        });
    }

    public void process(GroupChatMessageContent messageContent) {
        logger.info("消息开始处理：{}", messageContent.getMessageId());
//        String fromId = messageContent.getFromId();
//        String toId = messageContent.getToId();
//        Integer appId = messageContent.getAppId();
        //前置校验
        //这个用户是否被禁言 是否被禁用
        //发送方和接收方是否是好友
        GroupChatMessageContent messageFromMessageIdCache = messageStoreService.getMessageFromMessageIdCache(messageContent.getAppId(),
                messageContent.getMessageId(), GroupChatMessageContent.class);
        if (messageFromMessageIdCache != null) {
            threadPoolExecutor.execute(() -> {
                //1.回ack成功给自己
                ack(messageContent, ResponseVO.successResponse());
                //2.发消息给同步在线端
                syncToSender(messageContent, messageContent);
                //3.发消息给对方在线端
                dispatchMessage(messageContent);
            });
        }
        long seq = redisSeq.doGetSeq(messageContent.getAppId() + ":" + Constants.SeqConstants.GroupMessage
                + messageContent.getGroupId());
        messageContent.setMessageSequence(seq);
//        ResponseVO responseVO = imServerPermissionCheck(fromId, toId, appId);
//        if (responseVO.isOk()) {
        threadPoolExecutor.execute(() -> {
            //插入数据
            messageStoreService.storeGroupMessage(messageContent);
            //插入离线消息
            List<String> groupMemberId = imGroupMemberService.getGroupMemberId(messageContent.getGroupId(),
                    messageContent.getAppId());
            messageContent.setMemberId(groupMemberId);
            OfflineMessageContent offlineMessageContent = new OfflineMessageContent();
            BeanUtils.copyProperties(messageContent, offlineMessageContent);
            offlineMessageContent.setToId(messageContent.getGroupId());
            messageStoreService.storeGroupOfflineMessage(offlineMessageContent, groupMemberId);
            //1.回ack成功给自己
            ack(messageContent, ResponseVO.successResponse());
            //2.发消息给同步在线端
            syncToSender(messageContent, messageContent);
            //把messageId存进redis
            messageStoreService.setMessageFromMessageIdCache(messageContent.getAppId(),
                    messageContent.getMessageId(), messageContent);
            //3.发消息给对方在线端
            dispatchMessage(messageContent);
            logger.info("消息处理完成：{}", messageContent.getMessageId());
        });
//        } else {
//            //告诉客户端失败了
//            //ack
//            ack(messageContent, responseVO);
//        }
    }

    private void dispatchMessage(GroupChatMessageContent messageContent) {
        List<String> groupMemberId = imGroupMemberService.getGroupMemberId(messageContent.getGroupId(),
                messageContent.getAppId());
        for (String memberId : groupMemberId) {
            if (!memberId.equals(messageContent.getFromId())) {
                messageProducer.sendToUser(memberId,
                        GroupEventCommand.MSG_GROUP,
                        messageContent, messageContent.getAppId());
            }
        }
    }

    private void syncToSender(GroupChatMessageContent messageContent, ClientInfo clientInfo) {
        messageProducer.sendToUserExceptClient(messageContent.getFromId(), GroupEventCommand.MSG_GROUP, messageContent,
                messageContent);
    }

    private void ack(GroupChatMessageContent messageContent, ResponseVO responseVO) {
        logger.info("msg ack,msgId={},checkResut{}", messageContent.getMessageId(), responseVO.getCode());
        ChatMessageAck chatMessageAck = new ChatMessageAck(messageContent.getMessageId());
        responseVO.setData(chatMessageAck);
        //发消息
        messageProducer.sendToUser(messageContent.getFromId(), GroupEventCommand.GROUP_MSG_ACK, responseVO, messageContent);
    }

    private ResponseVO imServerPermissionCheck(String fromId, String toId, Integer appId) {
        ResponseVO responseVO = checkSendMessageService.checkGroupMessage(fromId, toId, appId);
        return responseVO;
    }

    public SendMessageResp send(SendGroupMessageReq req) {
        SendMessageResp sendMessageResp = new SendMessageResp();
        GroupChatMessageContent message = new GroupChatMessageContent();
        BeanUtils.copyProperties(req, message);
        messageStoreService.storeGroupMessage(message);
        sendMessageResp.setMessageKey(message.getMessageKey());
        sendMessageResp.setMessageTime(System.currentTimeMillis());
        //2.发消息给同步在线端
        syncToSender(message, message);
        //3.发消息给对方在线端
        dispatchMessage(message);
        return sendMessageResp;
    }
}
