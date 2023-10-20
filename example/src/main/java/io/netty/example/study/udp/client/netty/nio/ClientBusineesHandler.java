package io.netty.example.study.udp.client.netty.nio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.study.common.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientBusineesHandler extends SimpleChannelInboundHandler<ResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        log.info("received: {}", msg.getMessageBody());
    }
}
