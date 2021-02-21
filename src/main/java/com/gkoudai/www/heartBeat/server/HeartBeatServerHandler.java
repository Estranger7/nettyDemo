package com.gkoudai.www.heartBeat.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by：Estranger
 * Description：
 * Date：2021/2/21 15:27
 */
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
            .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
                    CharsetUtil.UTF_8));

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("收到Client " + incoming.remoteAddress() + "发来的心跳消息：" + s);
        //发回心跳消息
        for (Channel channel : channels) {
            if (channel != incoming) {
//                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
            } else {
                channel.writeAndFlush("hearbeat" + "\n");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress()+"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("Client:" + channel.remoteAddress() + "下线");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()){
                case WRITER_IDLE:
                    break;
                case READER_IDLE:
                    ctx.close().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            System.out.println("ChannelFuture hashCode {}:超时未收到心跳消息，主动断开连接" + future.channel().hashCode());
                        }
                    });
                case ALL_IDLE:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    //每当从服务端收到客户端断开时，客户端的 Channel 自动从 ChannelGroup 列表中移除了，并通知列表中的其他客户端 Channel
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
    }

}
