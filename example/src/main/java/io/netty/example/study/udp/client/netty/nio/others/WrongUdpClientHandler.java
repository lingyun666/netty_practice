package io.netty.example.study.udp.client.netty.nio.others;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.RequestMessage;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.DatagramPacket;
import java.util.List;
//use
public class WrongUdpClientHandler extends MessageToMessageEncoder<RequestMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        try {
            msg.encode(buffer);
            byte[] bytes = ByteBufUtil.getBytes(buffer);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
            out.add(datagramPacket);
        }finally{
            buffer.release();
        }
    }
}
