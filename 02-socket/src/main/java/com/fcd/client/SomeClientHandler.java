package com.fcd.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * SimpleChannelInboundHandler 􏰚􏰗中的 channelRead() 方法 会 自动释放接收到的来自对方的 msg 占有的所有资源
 * ChannelInboundHandlerAdapter 􏰚􏰗中的 channelRead() 方法 不会 自动释放接收到的来自对方的 msg
 */
public class SomeClientHandler extends SimpleChannelInboundHandler<String> {

    // msg的消费类型与类中的泛型类型是一致的
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        ctx.channel().writeAndFlush("from client: " + LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    // 当channel被激活后会出发该方法的执行
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("from client: begin connecting ...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
