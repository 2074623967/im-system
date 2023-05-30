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

/**
 * @author tangcj
 * @date 2023/05/30 16:07
 **/
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Integer command = msg.getMessageHeader().getCommand();
        //登录command
        if (command == SystemCommand.LOGIN.getCommand()) {
            LoginPack loginPack = JSON.parseObject(JSONObject.toJSONString(msg.getMessagePack()),
                    new TypeReference<LoginPack>() {
                    }.getType());
            String userId = loginPack.getUserId();
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
            //Redis map
            UserSession userSession = new UserSession();
            userSession.setAppId(msg.getMessageHeader().getAppId());
            userSession.setClientType(msg.getMessageHeader().getClientType());
            userSession.setUserId(loginPack.getUserId());
            userSession.setConnectState(ImConnectStatusEnum.ONLINE_STATUS.getCode());
            //将channel存起来
            RedissonClient redissonClient = RedisManager.getRedissonClient();
            RMap<String, String> map = redissonClient.
                    getMap(msg.getMessageHeader().getAppId() +
                            Constants.RedisConstants.UserSessionConstants +
                            loginPack.getUserId());
            map.put(msg.getMessageHeader().getClientType() +
                            ":" + msg.getMessageHeader().getImei(),
                    JSONObject.toJSONString(userSession));
            SessionSocketHolder.put(loginPack.getUserId(), (NioSocketChannel) ctx.channel());
        } else {

        }
    }
}
