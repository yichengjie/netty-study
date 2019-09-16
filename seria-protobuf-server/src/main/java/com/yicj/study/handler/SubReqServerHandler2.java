package com.yicj.study.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yicj.study.vo.SubscribeReq;
import com.yicj.study.vo.SubscribeResp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class SubReqServerHandler2 extends ChannelInboundHandlerAdapter {
	private final Logger logger = LoggerFactory.getLogger(SubReqServerHandler2.class);
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("客户端连接成功!" + ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.info("客户端断开连接!{}", ctx.channel().remoteAddress());
		ctx.channel().close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		SubscribeReq req = (SubscribeReq) msg;
		logger.info("server receive client : " + req.toString());
		SubscribeResp resp = new SubscribeResp() ;
		resp.setSubReqID(req.getSubReqID());
		resp.setRespCode(200);
		resp.setDesc("调用成功.....");
		ctx.writeAndFlush(resp);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) {
				logger.info("客户端已超过60秒未读写数据,关闭连接.{}", ctx.channel().remoteAddress());
				ctx.channel().close();
			}
		} else {
			logger.info("其他事件....");
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.info(cause.getMessage());
		ctx.close();
	}
}
