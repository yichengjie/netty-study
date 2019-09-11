package com.yicj.study;

import com.yicj.study.vo.Header;
import com.yicj.study.vo.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import io.netty.channel.ChannelHandler.Sharable ;

@Slf4j
@Sharable///标记一个ChannelHandler可以被多个Channel安全共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message msg1 = (Message) msg ;
		log.info("server receive client msg : " + msg1.toString());
		//此处写接受到客户端请求后的业务逻辑
		String content = "Hello world ,this is netty server!" ;
		byte tag = 0 ;
		byte encode = 1 ;
		byte encrypt = 1 ;
		byte extend1 = 1 ;
		byte extend2 = 0 ;
		String sessionid = "713f17ca614361fb257dc6741332caf2" ;
		int length = content.getBytes("UTF-8").length ;
		int cammand = 1 ;
		Header header = new Header(tag, encode, encrypt, extend1, extend2, sessionid, length, cammand) ;
		Message message = new Message(header, content) ;
		ctx.writeAndFlush(message) ;
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.info("EchoServerHandler.channelReadComplete()...");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//记录异常信息
		log.error("异常：",cause);
		//关闭channel
		ctx.close() ;
	}
}
