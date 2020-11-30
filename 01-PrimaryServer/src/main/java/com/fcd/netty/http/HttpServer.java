package com.fcd.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *  @Description: 服务器启动类
 *                创建并初始化服务器启动对象ServerBootStrap
 *  @Author: fanchengde
 *  @Data: 2020/11/26 3:01 下午
 */
public class HttpServer {
    public static void main(String[] args) throws InterruptedException {
        /*
          EventLoopGroup 是一个 EventLoop 池，包含很多的 EventLoop。
          Netty 为每个 Channel 分配了一个 EventLoop，用于处理用户连接请求、对用户请求的处理等所有事件。
          EventLoop 本身是一个线程驱动，在其生命周期内只会绑定一个线程，该线程处理一个 Channel 的所有 IO 事件。
          一个 Channel 一旦与一个 EventLoop 相绑定，那么在 Channel 的整个生命周期内是不能改变的。
          一个 EventLoop 可以与多个 Channel 绑定。即 Channel 与 EventLoop 的关系是 n:1， 而 EventLoop 与线程的关系是 1:1。
         */

        // parentGroup 中的 NioEventLoop 管理连接
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 每当 Client 发起一个连接，会创建一个 channel, 从 childGroup 中选出一个 NioEventLoop 与该channel绑定
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpChannelInitializer());

            ChannelFuture future = bootstrap.bind(8888).sync();
            future.channel().closeFuture().sync();

        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
