package io.netty.example.study.udp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.example.study.common.RequestMessage;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class ProtocolDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.decode(datagramPacket.content());

        UdpRequest udpRequest = new UdpRequest();
        udpRequest.setRequestMessage(requestMessage);
        udpRequest.setSender(datagramPacket.sender());

        out.add(udpRequest);
    }
}
