package com.gkoudai.www.nettyChat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by：Estranger
 * Description：TODO
 * Date：2020/4/28 9:55
 */
public class SimpleChatServer {

    public static final int port = 9000;

    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();//用来接收进来的连接
        EventLoopGroup workGroup = new NioEventLoopGroup();//用来处理已经被接收的连接,一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new SimpleChatServerHandler());
//                            System.out.println("SimpleChatClient："+ch.remoteAddress()+"连接上了");
                        }
                    });

            System.out.println("SimpleChatServer 启动了");

            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.out.println("SimpleChatServer 关闭了");
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleChatServer server = new SimpleChatServer();
        server.run();
    }
}
