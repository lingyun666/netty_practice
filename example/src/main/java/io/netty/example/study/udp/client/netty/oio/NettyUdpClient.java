package io.netty.example.study.udp.client.netty.oio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.udp.client.netty.nio.ClientBusineesHandler;
import io.netty.example.study.udp.client.netty.nio.ProtocolDecoder;
import io.netty.example.study.udp.client.netty.nio.ProtocolEncoder;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class NettyUdpClient {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new OioEventLoopGroup());
        bootstrap.channel(OioDatagramChannel.class);
        final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
        bootstrap.handler(new ChannelInitializer<OioDatagramChannel>() {
            @Override
            protected void initChannel(OioDatagramChannel ch) throws Exception {
                ch.pipeline().addLast(loggingHandler);
                ch.pipeline().addLast(new ProtocolDecoder());
                ch.pipeline().addLast(new ProtocolEncoder());
                ch.pipeline().addLast(new ClientBusineesHandler());
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);
        channelFuture.sync();

        RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "tudou"));
        ChannelFuture channelFuture1 = channelFuture.channel().writeAndFlush(requestMessage);
        channelFuture1.get();

     }


}


