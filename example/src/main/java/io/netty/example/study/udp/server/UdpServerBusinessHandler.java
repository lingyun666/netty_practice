package io.netty.example.study.udp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.OperationResult;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.ResponseMessage;

public class UdpServerBusinessHandler extends SimpleChannelInboundHandler<UdpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UdpRequest udpRequest) throws Exception {
        RequestMessage requestMessage = udpRequest.getRequestMessage();
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        UdpResponse udpResponse = new UdpResponse();
        udpResponse.setResponseMessage(responseMessage);
        udpResponse.setReceiver(udpRequest.getSender());

        ctx.channel().writeAndFlush(udpResponse);
    }

}
