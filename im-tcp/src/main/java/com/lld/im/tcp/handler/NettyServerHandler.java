package com.lld.im.tcp.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lld.im.codec.pack.LoginPack;
import com.lld.im.codec.pack.message.ChatMessageAck;
import com.lld.im.codec.proto.Message;
import com.lld.im.codec.proto.MessagePack;
import com.lld.im.common.ResponseVO;
import com.lld.im.common.constant.Constants;
import com.lld.im.common.enums.ImConnectStatusEnum;
import com.lld.im.common.enums.command.GroupEventCommand;
import com.lld.im.common.enums.command.MessageCommand;
import com.lld.im.common.enums.command.SystemCommand;
import com.lld.im.common.model.UserClientDto;
import com.lld.im.common.model.UserSession;
import com.lld.im.common.model.message.CheckSendMessageReq;
import com.lld.im.tcp.feign.FeignMessageService;
import com.lld.im.tcp.publish.MqMessageProducer;
import com.lld.im.tcp.redis.RedisManager;
import com.lld.im.tcp.utils.SessionSocketHolder;
import feign.Feign;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * @author tangcj
 * @date 2023/05/30 16:07
 **/
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class.getName());

    private Integer brokerId;

    private String logicUrl;

    private FeignMessageService feignMessageService;

    public NettyServerHandler(Integer brokerId, String logicUrl) {
        this.brokerId = brokerId;
        feignMessageService = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, 3500))//设置超时时间
                .target(FeignMessageService.class, logicUrl);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Integer command = msg.getMessageHeader().getCommand();
        //登录command
        if (command == SystemCommand.LOGIN.getCommand()) {
            LoginPack loginPack = JSON.parseObject(JSONObject.toJSONString(msg.getMessagePack()),
                    new TypeReference<LoginPack>() {
                    }.getType());
            String userId = loginPack.getUserId();
            ctx.channel().attr(AttributeKey.valueOf(Constants.UserId)).set(userId);
            ctx.channel().attr(AttributeKey.valueOf(Constants.AppId)).set(msg.getMessageHeader().getAppId());
            ctx.channel().attr(AttributeKey.valueOf(Constants.ClientType)).set(msg.getMessageHeader().getClientType());
            ctx.channel().attr(AttributeKey.valueOf(Constants.Imei)).set(msg.getMessageHeader().getImei());
            //Redis map
            UserSession userSession = new UserSession();
            userSession.setAppId(msg.getMessageHeader().getAppId());
            userSession.setClientType(msg.getMessageHeader().getClientType());
            userSession.setUserId(loginPack.getUserId());
            userSession.setConnectState(ImConnectStatusEnum.ONLINE_STATUS.getCode());
            userSession.setBrokerId(brokerId);
            userSession.setImei(msg.getMessageHeader().getImei());
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                userSession.setBrokerHost(localHost.getHostAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
            RedissonClient redissonClient = RedisManager.getRedissonClient();
            RMap<String, String> map = redissonClient.
                    getMap(msg.getMessageHeader().getAppId() +
                            Constants.RedisConstants.UserSessionConstants +
                            loginPack.getUserId());
            map.put(msg.getMessageHeader().getClientType() +
                            ":" + msg.getMessageHeader().getImei(),
                    JSONObject.toJSONString(userSession));
            //将channel存起来
            SessionSocketHolder.put(msg.getMessageHeader().getAppId(),
                    loginPack.getUserId(),
                    msg.getMessageHeader().getClientType(),
                    msg.getMessageHeader().getImei(),
                    (NioSocketChannel) ctx.channel());
            UserClientDto dto = new UserClientDto();
            dto.setImei(msg.getMessageHeader().getImei());
            dto.setUserId(loginPack.getUserId());
            dto.setClientType(msg.getMessageHeader().getClientType());
            dto.setAppId(msg.getMessageHeader().getAppId());
            RTopic topic = redissonClient.getTopic(Constants.RedisConstants.UserLoginChannel);
            topic.publish(JSONObject.toJSONString(dto));

        } else if (command == SystemCommand.LOGOUT.getCommand()) {
            //删除session
            //redis 删除
            SessionSocketHolder.removeUserSession((NioSocketChannel) ctx.channel());
        } else if (command == SystemCommand.PING.getCommand()) {
            ctx.channel().attr(AttributeKey.valueOf(Constants.ReadTime)).set(System.currentTimeMillis());
        } else if (command == MessageCommand.MSG_P2P.getCommand()
                || command == GroupEventCommand.MSG_GROUP.getCommand()) {
            String toId;
            CheckSendMessageReq req = new CheckSendMessageReq();
            req.setAppId(msg.getMessageHeader().getAppId());
            req.setCommand(msg.getMessageHeader().getCommand());
            JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(msg.getMessagePack()));
            String fromId = jsonObject.getString("fromId");
            if (command == MessageCommand.MSG_P2P.getCommand()) {
                toId = jsonObject.getString("toId");
            } else {
                toId = jsonObject.getString("groupId");
            }
            req.setToId(toId);
            req.setFromId(fromId);
            ResponseVO responseVO = feignMessageService.checkSendMessage(req);
            if (responseVO.isOk()) {
                //如果成功投递到mq
                MqMessageProducer.sendMessage(msg, command);
            } else {
                //失败则直接ack
                Integer ackCommand;
                if (command == MessageCommand.MSG_P2P.getCommand()) {
                    ackCommand = MessageCommand.MSG_ACK.getCommand();
                } else {
                    ackCommand = GroupEventCommand.GROUP_MSG_ACK.getCommand();
                }
                ChatMessageAck chatMessageAck = new ChatMessageAck(jsonObject.getString("messageId"));
                responseVO.setData(chatMessageAck);
                MessagePack<ResponseVO> ack = new MessagePack<>();
                ack.setData(responseVO);
                ack.setCommand(ackCommand);
                ctx.channel().writeAndFlush(ack);
            }
        } else {
            MqMessageProducer.sendMessage(msg, command);
        }
    }
}
