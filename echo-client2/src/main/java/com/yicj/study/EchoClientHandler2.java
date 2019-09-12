package com.yicj.study;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

import com.yicj.study.vo.Header;
import com.yicj.study.vo.Message;

import io.netty.channel.ChannelHandler.Sharable;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Sharable//标记该类的实例可以被多个Channel共享
public class EchoClientHandler2 extends ChannelInboundHandlerAdapter {
	
	//接收到数据后调用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message msg1 = (Message) msg ;
		log.info("EchoClientHandler2 receive message from server : " + msg1.toString());
	}
	
	
	//完成时调用
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("EchoClientHandler2.channelReadComplete");
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//发生异常时，记录错误并关闭Channel
		log.error("发生异常:",cause);
		ctx.close() ;
	}
}
