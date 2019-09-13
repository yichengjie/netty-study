package com.yicj.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Sharable//标记该类的实例可以被多个Channel共享
public class ClientChannelReadHandler extends SimpleChannelInboundHandler<ByteBuf> {
	//每当接收数据时都会调用这个方法。需要注意的是，由服务器发送的消息可能会被分块接收。也就是说服务器发送了5字节，那么不能保证
	//这5个字节会被一次接收。即使是对于这么少的数据，channelRead0()方法也可能会被调用两次，第一次使用一个持有3字节的ByteBuf
	//第二次使用一个持有2字节的ByteBuf。
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		//记录已接收消息的转储
		log.info("Client received : " + in.toString(CharsetUtil.UTF_8));
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//发生异常时，记录错误并关闭Channel
		log.error("发生异常:",cause);
		ctx.close() ;
	}
}
