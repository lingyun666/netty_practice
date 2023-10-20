package io.netty.example.study.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.common.order.OrderOperationResult;
import io.netty.example.study.util.JsonUtil;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        if(isOrderRequest(fullHttpRequest)){
            OrderOperation orderOperation = decodeRequestBodyAsObject(fullHttpRequest);
            OrderOperationResult orderOperationResult = orderOperation.execute();
            ByteBuf buffer = generateJsonAsResponseBody(ctx, orderOperationResult);
            DefaultFullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CREATED, buffer);

            msg.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            msg.headers().add(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());

            ctx.writeAndFlush(msg);
        }else{
            DefaultFullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            ctx.writeAndFlush(msg);
        }
    }

    private boolean isOrderRequest(FullHttpRequest fullHttpRequest) {
        return HttpMethod.POST.equals(fullHttpRequest.method()) && "/order".equalsIgnoreCase(fullHttpRequest.uri());
    }

    private OrderOperation decodeRequestBodyAsObject(FullHttpRequest fullHttpRequest) {
        ByteBuf content = fullHttpRequest.content();
        byte[] bytes = ByteBufUtil.getBytes(content);
        return JsonUtil.fromJson(bytes, OrderOperation.class);
    }

    private ByteBuf generateJsonAsResponseBody(ChannelHandlerContext ctx, OrderOperationResult orderOperationResult) {
        byte[] resultBytes  = JsonUtil.toJsonBytes(orderOperationResult);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(resultBytes);

        return buffer;
    }
}
