package com.gkoudai.www.heartBeat.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by：Estranger
 * Description：
 * Date：2021/2/21 15:49
 */
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println(s);
    }

}
