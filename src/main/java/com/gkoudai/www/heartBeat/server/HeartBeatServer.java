package com.gkoudai.www.heartBeat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by：Estranger
 * Description：
 * Date：2021/2/21 15:09
 */
public class HeartBeatServer{

    private final int port = 8080;

    public HeartBeatServer() {
    }

    private void init() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new HeartBeatServerInitializer());
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("server端启动成功：" + port);
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }

    }



    public static void main(String[] args) throws InterruptedException {
        new HeartBeatServer().init();
    }

}
