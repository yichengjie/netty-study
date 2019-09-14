package com.yicj.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable///标记一个ChannelHandler可以被多个Channel安全共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	//private ByteBuf in ;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg ;
		//this.in = in ;
		log.info("Server received: " + in.toString(CharsetUtil.UTF_8));
		//将接收到的消息写给发送者，而不冲刷出站消息
		ctx.write(msg) ;//write方法不会自动释放in的引用次数，需要在channelReadComplete中调用writeAndFlush方法
		//ctx.writeAndFlush(in) ;//这个会自动释放buf的引用次数
		//int refCnt = in.refCnt();
		//log.info("====> refCnt : " + refCnt);
		//因为在channelReadComplete中调用了writeAndFlush方法，所以这里不需要释放引用，否则报错
		//ReferenceCountUtil.release(in) ;
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//将未决消息冲刷到远程节点，并且关闭该channel
		//int refCnt21 = in.refCnt();
		//log.info("====> refCnt21 : " + refCnt21);
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) ;
		//.addListener(ChannelFutureListener.CLOSE) ;
		//int refCnt22 = in.refCnt();
		//log.info("====> refCnt22 : " + refCnt22);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//记录异常信息
		log.error("异常：",cause);
		//关闭channel
		ctx.close() ;
	}
}
