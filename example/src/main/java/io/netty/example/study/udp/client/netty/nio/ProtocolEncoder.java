package io.netty.example.study.udp.client.netty.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.example.study.common.RequestMessage;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.util.List;

public class ProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        msg.encode(buffer);
        DatagramPacket datagramPacket = new DatagramPacket(buffer, (InetSocketAddress) ctx.channel().remoteAddress());
        out.add(datagramPacket);
    }
}
