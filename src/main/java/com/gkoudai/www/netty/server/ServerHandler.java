package com.gkoudai.www.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * Created by：Estranger
 * Description：TODO
 * Date：2020/4/27 16:26
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("服务器接收："+in.toString(CharsetUtil.UTF_8));
        in.clear();
        String str = "Hello Netty-Client!";
        in.writeBytes(str.getBytes());
        ctx.writeAndFlush(in);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Netty-Client again!", CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
//        closeChannelAndReConnect(ctx);
    }

//    private void closeChannelAndReConnect(ChannelHandlerContext ctx) {
//        Channel channel = ctx.channel();
//        if(channel != null){
//            channel.close();
//            ctx.close();
//        }
//    }
}
