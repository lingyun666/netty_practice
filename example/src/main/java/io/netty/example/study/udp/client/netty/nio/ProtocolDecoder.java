package io.netty.example.study.udp.client.netty.nio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.example.study.common.ResponseMessage;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class ProtocolDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(msg.content());
        out.add(responseMessage);
    }
}
