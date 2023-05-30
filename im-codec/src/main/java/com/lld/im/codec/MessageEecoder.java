package com.lld.im.codec;

import com.alibaba.fastjson.JSONObject;
import com.lld.im.codec.proto.MessagePack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 消息编码类,私有协议规则,前4位表示长度,接着command4位,后面是数据
 *
 * @author tangcj
 * @date 2023/05/30 13:56
 **/
public class MessageEecoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof MessagePack) {
            MessagePack msgBody = (MessagePack) msg;
            String s = JSONObject.toJSONString(msgBody);
            byte[] bytes = s.getBytes();
            out.writeIntLE(msgBody.getCommand());
            out.writeIntLE(bytes.length);
            out.writeBytes(bytes);
        }
    }
}
