package com.yicj.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import io.netty.channel.ChannelHandler.Sharable ;

@Slf4j
@Sharable///标记一个ChannelHandler可以被多个Channel安全共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg ;
		log.info("Server received: " + in.toString(CharsetUtil.UTF_8));
		//将接收到的消息写给发送者，而不冲刷出站消息
		ctx.write(in) ;
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
		//将未决消息冲刷到远程节点，并且关闭该channel
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
		.addListener(ChannelFutureListener.CLOSE) ;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//记录异常信息
		log.error("异常：",cause);
		//关闭channel
		ctx.close() ;
	}
}
