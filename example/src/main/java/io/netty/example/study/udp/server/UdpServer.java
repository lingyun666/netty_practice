package io.netty.example.study.udp.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class UdpServer {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(5000));
        bootstrap.channel(NioDatagramChannel.class);
        final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
        bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel ch) throws Exception {
              ch.pipeline().addLast(loggingHandler);
                ch.pipeline().addLast(new ProtocolDecoder());
                ch.pipeline().addLast(new ProtocolEncoder());
                ch.pipeline().addLast(new UdpServerBusinessHandler());
            }
        });

        bootstrap.bind(8090).sync().channel().closeFuture().await();
    }

}
