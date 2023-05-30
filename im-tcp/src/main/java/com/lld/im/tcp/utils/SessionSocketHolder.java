package com.lld.im.tcp.utils;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tangcj
 * @date 2023/05/30 16:47
 **/
public class SessionSocketHolder {

    private static final Map<String, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    public static void put(String userId, NioSocketChannel channel) {
        CHANNELS.put(userId, channel);
    }

    public static void get(String userId) {
        CHANNELS.get(userId);
    }
}
