package com.bruis.learnnetty.rpc.client;

import com.alibaba.fastjson.JSONObject;
import com.bruis.learnnetty.rpc.utils.RequestFuture;
import com.bruis.learnnetty.rpc.utils.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lhy
 * @date 2022/2/11
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        RequestFuture.received(response);
    }
}
