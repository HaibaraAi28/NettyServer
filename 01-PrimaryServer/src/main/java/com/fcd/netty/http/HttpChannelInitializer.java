package com.fcd.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 *  @Description: 管道初始化器
 *  当前类的实例在pipeline初始化完毕后就会被GC(所以一般直接用匿名内部类实现，见PrimaryServer2)
 *  @Author: fanchengde
 *  @Data: 2020/11/26 3:26 下午
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 当Channel初始创建完毕后就会触发该方法的执行，用于初始化Channel
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 从Channel中获取pipeline
        ChannelPipeline pipeline = ch.pipeline();
        // 都用addLast，先放入的先执行
        // 将HttpServerCodec处理器放入到pipeline的最后
        // HttpServerCodec是HttpRequestDecoder与HttpResponseEncoder的复合体
        // HttpRequestDecoder：http请求解码器，将Channel中的ByteBuf数据解码为HttpRequest对象
        // HttpResponseEncoder：http响应编码器，将HttpResponse对象编码为将要在Channel中发送的ByteBuf数据
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        // 将自定义的处理器放入到Pipeline的最后
        pipeline.addLast("httpServerHandler", new HttpServerHandler());
    }
}
