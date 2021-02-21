package com.gkoudai.www.heartBeat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by：Estranger
 * Description：
 * Date：2021/2/21 15:42
 */
public class HeartBeatClient {

    private final String serverAddress = "localhost";

    private final int port = 8080;


    private void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new HeartBeatClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(serverAddress,port).sync();

            //控制台输入文字，刷入通道中
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                future.channel().writeAndFlush(in.readLine() +"\r\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new HeartBeatClient().run();
    }
}
