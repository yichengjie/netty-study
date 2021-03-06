package com.yicj.study.handler;

import com.yicj.study.vo.SubscribeReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubReqClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//在链路激活的时候循环构造10条订购消息，最后一次性地发送给服务器端
		log.info("SubReqClientHandler channelActive exec ....");
		for(int i = 0 ; i < 10 ; i++) {
			ctx.write(subReq(i)) ;
		}
		ctx.flush();
		ctx.fireChannelInactive() ;
	}
	
	private SubscribeReq subReq(int i) {
		SubscribeReq req = new SubscribeReq() ;
		req.setUserName("yicj");
		req.setAddress("北京市朝阳区天辰东路奥林匹克公园");
		req.setPhoneNumber("157xxxxxxxx");
		req.setProductName("Netty for marshalling");
		req.setSubReqID(i);
		return req ;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//由于对象解码器已经对订购请求应答消息进行了自动解码
		//因此，SubReqClientHandler接收到的消息已经是解码成功后的订购应答消息
		log.info("Receive server response :["+msg.toString()+"]");
		ctx.fireChannelRead(msg) ;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("出错:" ,cause);
		ctx.close() ;
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			log.info("准备发送心跳数据.....");
			SubscribeReq subReq = subReq(-1000);
			ctx.channel().writeAndFlush(subReq) ;
		}else {
			super.userEventTriggered(ctx, evt);
		}
		
	}
	
}
