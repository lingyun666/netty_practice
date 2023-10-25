package com.bruis.learnnetty.im.server.handler;

import com.bruis.learnnetty.im.model.LoginRequestPacket;
import com.bruis.learnnetty.im.model.LoginResponsePacket;
import com.bruis.learnnetty.im.session.Session;
import com.bruis.learnnetty.im.util.IDUtil;
import com.bruis.learnnetty.im.util.SessionUtil;
import io.netty.channel.*;

import java.util.Arrays;
import java.util.Date;

/**
 * @Description 接收客户端登录请求
 * @Author luohaiyang
 * @Date 2022/3/23
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    protected LoginRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        // 登录校验响应
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());

        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            String userId = IDUtil.randomUserId();
            loginResponsePacket.setUserId(userId);
            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUserName()), ctx.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.writeAndFlush(loginResponsePacket).addListener((ChannelFutureListener) future -> {
            // 关闭channel成功
            Throwable cause = future.cause();
            if (null != cause) {
                System.out.println(Arrays.toString(cause.getStackTrace()));
            }
        });
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
