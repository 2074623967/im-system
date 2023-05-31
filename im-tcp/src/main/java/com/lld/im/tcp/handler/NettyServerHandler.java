package com.lld.im.tcp.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lld.im.codec.pack.LoginPack;
import com.lld.im.codec.proto.Message;
import com.lld.im.common.constant.Constants;
import com.lld.im.common.enums.ImConnectStatusEnum;
import com.lld.im.common.enums.command.SystemCommand;
import com.lld.im.common.model.UserSession;
import com.lld.im.tcp.redis.RedisManager;
import com.lld.im.tcp.utils.SessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.redisson.api.RMap;
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

    public NettyServerHandler(Integer brokerId) {
        this.brokerId = brokerId;
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
            //Redis map
            UserSession userSession = new UserSession();
            userSession.setAppId(msg.getMessageHeader().getAppId());
            userSession.setClientType(msg.getMessageHeader().getClientType());
            userSession.setUserId(loginPack.getUserId());
            userSession.setConnectState(ImConnectStatusEnum.ONLINE_STATUS.getCode());
            userSession.setBrokerId(brokerId);
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

        } else if (command == SystemCommand.LOGOUT.getCommand()) {
            //删除session
            //redis 删除
            SessionSocketHolder.removeUserSession((NioSocketChannel) ctx.channel());
        } else if (command == SystemCommand.PING.getCommand()) {
            ctx.channel().attr(AttributeKey.valueOf(Constants.ReadTime)).set(System.currentTimeMillis());
        } else {

        }
    }
}
