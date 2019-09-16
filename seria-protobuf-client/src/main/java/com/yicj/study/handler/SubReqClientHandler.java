package com.yicj.study.handler;

import java.util.ArrayList;
import java.util.List;

import com.yicj.study.proto.SubscribeReqProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubReqClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//在链路激活的时候循环构造10条订购消息，最后一次性地发送给服务器端
		for(int i = 0 ; i < 10 ; i++) {
			ctx.write(subReq(i)) ;
		}
		ctx.flush();
	}
	
	
	private Object subReq(int reqId) {
		SubscribeReqProto.SubscribeReq.Builder builder = 
				SubscribeReqProto.SubscribeReq.newBuilder() ;
		builder.setProductName("Netty Boot") ;
		builder.setUserName("yicj") ;
		builder.setSubReqId(reqId) ;
		List<String> address = new ArrayList<>() ;
		address.add("aaaaaaaaaaaaaaaaaaa") ;
		address.add("bbbbbbbbbbbbbbbbbbb") ;
		address.add("ccccccccccccccccccc") ;
		builder.addAllAddress(address) ;
		return builder.build();
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("接收服务器端消息是 :["+msg.toString()+"]");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush() ;
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			ctx.channel().writeAndFlush(subReq(-1000)) ;
		}else {
			super.userEventTriggered(ctx, evt);
		}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("出错:" ,cause);
		ctx.close() ;
	}
	
}
