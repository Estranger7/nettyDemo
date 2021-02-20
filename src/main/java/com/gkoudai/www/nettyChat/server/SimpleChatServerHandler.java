package com.gkoudai.www.nettyChat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by：Estranger
 * Description：TODO
 * Date：2020/4/28 9:40
 */
//SimpleChannelInboundHandler这个类实现了 ChannelInboundHandler 接口,ChannelInboundHandler 提供了许多事件处理的接口方法
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String>{

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);



    //每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel。其中如果你使用的是 Netty 5.x 版本时，需要把 channelRead0() 重命名为messageReceived()
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
            } else {
                channel.writeAndFlush("[you]" + s + "\n");
            }
        }
    }

    //服务端监听到客户端活动
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"在线");
    }

    //服务端监听到客户端不活动
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"掉线");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * handler被添加到channel的时候执行，这个动作就是由pipeline的添加handler方法完成的。对于服务端，在客户端连接进来的时候，就通过ServerBootstrapAcceptor的       * read方法，为每一个channel添加了handler。该方法对于handler而言是第一个触发的方法。
     *
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"加入了");
        //每当从服务端收到新的客户端连接时，客户端的 Channel 存入 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
        channels.add(ctx.channel());
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + "加入\n");
    }

    //每当从服务端收到客户端断开时，客户端的 Channel 自动从 ChannelGroup 列表中移除了，并通知列表中的其他客户端 Channel
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
    }
}
