package com.yicj.study;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.yicj.study.vo.Header;
import com.yicj.study.vo.Message;

import io.netty.channel.ChannelHandler.Sharable;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Sharable//标记该类的实例可以被多个Channel共享
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("channel active"); 
		String content = "EchoClientHandler hello world ,this is netty client !" ;
		byte tag = 0 ;
		byte encode = 1 ;
		byte encrypt = 1;
		byte extend1 = 1 ;
		byte extend2 = 0 ;
		String sessionid = "713f17ca614361fb257dc6741332caf2" ;
		int length = content.getBytes("UTF-8").length ;
		int cammand = 1 ;
		Header header = new Header(tag, encode, encrypt, extend1, extend2, sessionid, length, cammand) ;
		Message message = new Message(header, content) ;
		ctx.writeAndFlush(message) ;
	}
	
	//接收到数据后调用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message msg1 = (Message) msg ;
		log.info("EchoClientHandler client receive message from server : " + msg1.toString());
		//如果重新channelRead方法则必须要手动调用fireChannelRead，将数据传递给下一个channelHandler
		ctx.fireChannelRead(msg) ;
	}
	
	
	//完成时调用
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelReadComplete");
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//发生异常时，记录错误并关闭Channel
		log.error("发生异常:",cause);
		ctx.close() ;
	}
}
