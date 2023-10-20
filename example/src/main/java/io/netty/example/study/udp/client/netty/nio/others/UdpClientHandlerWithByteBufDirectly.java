package io.netty.example.study.udp.client.netty.nio.others;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.RequestMessage;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class UdpClientHandlerWithByteBufDirectly extends MessageToMessageEncoder<RequestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        msg.encode(buffer);
        out.add(buffer);
    }
}
