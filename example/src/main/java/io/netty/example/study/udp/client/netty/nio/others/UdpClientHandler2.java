package io.netty.example.study.udp.client.netty.nio.others;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.RequestMessage;
import io.netty.handler.codec.MessageToByteEncoder;

public class UdpClientHandler2 extends MessageToByteEncoder<RequestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage msg, ByteBuf out) throws Exception {
        msg.encode(out);
    }
}
