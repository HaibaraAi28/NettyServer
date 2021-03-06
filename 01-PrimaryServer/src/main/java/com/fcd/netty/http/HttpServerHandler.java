package com.fcd.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 *  @Description: 自定义服务器端处理器
 *  需求：用户提交一个请求后，在浏览器上就会看到hello netty world
 *  @Author: fanchengde
 *  @Data: 2020/11/26 3:39 下午
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当Channel中有来自于客户端的数据时就会触发该方法的执行
     * @param ctx  上下文对象
     * @param msg   就是来自于客户端的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("请求方式: " + request.method().name());
            System.out.println("请求URI: " + request.uri());

            if ("/favicon.ico".equals(request.uri())) {
                System.out.println("不处理/favicon.ico请求");
                return;
            }

            // 构造response的响应体
            ByteBuf content = Unpooled.copiedBuffer("Hello Netty World", CharsetUtil.UTF_8);
            // 生成响应对象
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            // 获取到response的头部后进行初始化
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将响应对象写入到Channel
            // ctx.write(response);
            // ctx.flush();
            // ctx.writeAndFlush(response);
            // ctx.channel().close();
            ctx.writeAndFlush(response)
                    // 添加channel关闭监听器
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     *  当Channel中的数据在处理过程中出现异常时会触发该方法的执行
     * @param ctx  上下文
     * @param cause  发生的异常对象
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭Channel 触发closeFuture()
        ctx.close();
    }
}
