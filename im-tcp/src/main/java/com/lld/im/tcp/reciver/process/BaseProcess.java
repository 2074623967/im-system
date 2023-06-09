package com.lld.im.tcp.reciver.process;

import com.lld.im.codec.proto.MessagePack;
import com.lld.im.tcp.utils.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author tangcj
 * @date 2023/06/03 21:59
 **/
public abstract class BaseProcess {

    public abstract void processBefore();

    public void process(MessagePack messagePack) {
        processBefore();
        NioSocketChannel channel = SessionSocketHolder.get(messagePack.getAppId(), messagePack.getToId(),
                messagePack.getClientType(), messagePack.getImei());
        if (channel != null) {
            channel.writeAndFlush(messagePack);
        }
        processAfter();
    }

    public abstract void processAfter();

}
