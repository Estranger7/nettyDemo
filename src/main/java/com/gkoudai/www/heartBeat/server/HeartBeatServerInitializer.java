package com.gkoudai.www.heartBeat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by：Estranger
 * Description：
 * Date：2021/2/21 15:20
 */
public class HeartBeatServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final int READ_IDEL_TIME_OUT = 4; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 5;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 7; // 所有超时

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline
                .addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
                .addLast(new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT))
                .addLast(new HeartBeatServerHandler());
    }
}
