package io.netty.example.study.client.handler.newclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.client.codec.OperationToRequestMessageEncoder;
import io.netty.example.study.client.codec.OrderFrameDecoder;
import io.netty.example.study.client.codec.OrderFrameEncoder;
import io.netty.example.study.client.codec.OrderProtocolDecoder;
import io.netty.example.study.client.codec.OrderProtocolEncoder;
import io.netty.example.study.client.handler.dispatcher.OperationResultFuture;
import io.netty.example.study.client.handler.dispatcher.RequestPendingCenter;
import io.netty.example.study.client.handler.dispatcher.ResponseDispatcherHandler;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.OperationResult;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.common.order.OrderOperationResult;
import io.netty.example.study.common.order.QueryOrderOperation;
import io.netty.example.study.common.order.QueryOrderOperationResult;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * just to demo async implement without auth/ssl/etc
 */
public class AsyncClient implements AsyncOperationable {

    private RequestPendingCenter requestPendingCenter;
    private Channel channel;

    AsyncClient(String serverAddress, int serverPort){
        this.requestPendingCenter = new RequestPendingCenter();
        Bootstrap bootstrap = new Bootstrap();
        initialBootstrap(bootstrap, requestPendingCenter);
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(serverAddress, serverPort).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.channel = channelFuture.channel();
    }

    private void initialBootstrap(Bootstrap bootstrap, RequestPendingCenter requestPendingCenter) {
        bootstrap.channel(NioSocketChannel.class);
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group);

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(new ResponseDispatcherHandler(requestPendingCenter));

                pipeline.addLast(new OperationToRequestMessageEncoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });
    }

    @Override
    public OperationResultFuture<OrderOperationResult> order(OrderOperation orderOperation) {
        return getOrderOperationResult(orderOperation);
    }

    private <T extends OperationResult> OperationResultFuture<T> getOrderOperationResult(Operation operation){
        long streamId = IdUtil.nextId();
        RequestMessage requestMessage = new RequestMessage(
                streamId, operation);
        OperationResultFuture<T> operationResultFuture = new OperationResultFuture();
        requestPendingCenter.add(streamId, operationResultFuture);
        this.channel.writeAndFlush(requestMessage);
        return operationResultFuture;
    }

    @Override
    public OperationResultFuture<QueryOrderOperationResult> queryOrder(QueryOrderOperation queryOrderOperation){
        return getOrderOperationResult(queryOrderOperation);
    }

    public static void main(String[] args) throws Exception {
        AsyncClient asyncClient = new AsyncClient("127.0.0.1", 8090);
        OperationResultFuture<OrderOperationResult> future = asyncClient.order(new OrderOperation(1001, "tudou"));
        OrderOperationResult orderOperationResult = future.get();
        System.out.println(orderOperationResult);
    }

}
