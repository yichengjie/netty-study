package com.yicj.study.channel;

import com.yicj.study.vo.SubscribeReq;
import com.yicj.study.vo.SubscribeResp;

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
		SubscribeReq req = (SubscribeReq) msg ;
		//if("yicj".equalsIgnoreCase(req.getUserName())) {
		log.info("Service accept client subscribe req:["+req.toString()+"]");
		//对订购者的用户名进行合法性验证，校验通过后打印订购请求信息，
		//构造订购成功应答消息立即发送给客户端
		ctx.writeAndFlush(resp(req.getSubReqID())) ;
		//}
	}
	
	private SubscribeResp resp(int subReqID) {
		SubscribeResp resp = new SubscribeResp() ;
		resp.setSubReqID(subReqID); 
		resp.setRespCode(0);
		resp.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
		return resp ;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("出错 : " ,cause);
		ctx.close() ;
	}
}
