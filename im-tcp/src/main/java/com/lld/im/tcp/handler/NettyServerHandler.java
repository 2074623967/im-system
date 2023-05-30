package com.lld.im.tcp.handler;

import com.lld.im.codec.proto.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tangcj
 * @date 2023/05/30 16:07
 **/
public class NettyServerHandler  extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println(msg);
    }
}
