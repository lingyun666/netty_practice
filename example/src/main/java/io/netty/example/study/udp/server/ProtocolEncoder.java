package io.netty.example.study.udp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ProtocolEncoder extends MessageToMessageEncoder<UdpResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, UdpResponse udpResponse , List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        udpResponse.getResponseMessage().encode(buffer);
       // DatagramPacket datagramPacket = new DatagramPacket(buffer, (InetSocketAddress) ctx.channel().remoteAddress());
        DatagramPacket datagramPacket = new DatagramPacket(buffer, udpResponse.getReceiver());
        out.add(datagramPacket);
    }
}
