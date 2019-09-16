package com.yicj.study.handler;

import com.yicj.study.proto.SubscribeReqProto;
import com.yicj.study.proto.SubscribeRespProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubReqServerHandler extends ChannelInboundHandlerAdapter {
	@Override 
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//log.info("SubReqServerHandler  channelRead method is call ...");
		//经过解码器handler ObjectDecoder的解码
		//SubReqServerHandler接收到的请求消息已经被自动解码为SubscribeReq对象，可以直接使用
		SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq)msg ;
		log.info("服务器接收客户端消息是 : " + req.toString());
		ctx.writeAndFlush(resp(req.getSubReqId())) ;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("出错 : " ,cause);
		ctx.close() ;
	}
	
	private SubscribeRespProto.SubscribeResp resp (int subReqId){
		SubscribeRespProto.SubscribeResp.Builder builder = 
				SubscribeRespProto.SubscribeResp.newBuilder() ;
		builder.setSubReqId(subReqId) ;
		builder.setRespCode(200) ;
		builder.setDesc("网上预订成功3天后，发送到指定的地址") ;
		return builder.build() ;
	}
	
}
