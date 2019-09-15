package com.yicj.study.handler;

import java.net.InetSocketAddress;

import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yicj.study.vo.SubscribeReq;
import com.yicj.study.vo.SubscribeResp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubReqClientHandler2  extends ChannelInboundHandlerAdapter{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void channelActive(ChannelHandlerContext ctx)   {
        logger.info("已连接到RPC服务器.{}",ctx.channel().remoteAddress());
        SubscribeReq req = new SubscribeReq() ;
        req.setSubReqID(1001);
        req.setUserName("heartBeat");
        req.setAddress("bjs");
        req.setPhoneNumber("1002");
        req.setPhoneNumber("100xxxx"); 
        ctx.channel().writeAndFlush(req) ;
    }
	@Override
    public void channelInactive(ChannelHandlerContext ctx)   {
        InetSocketAddress address =(InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("与RPC服务器断开连接."+address);
        ctx.channel().close();
    }
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
    	SubscribeResp response = (SubscribeResp) msg ;
        log.info("client receive server : " + response.toString());
    }
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        logger.info("RPC通信服务器发生异常.{}",cause);
        ctx.channel().close();
    }
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
        logger.info("已超过30秒未与RPC服务器进行读写操作!将发送心跳消息...");
        if (evt instanceof IdleStateEvent){
        	logger.info("准备发送心跳数据.....");
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.ALL_IDLE){
            	SubscribeReq req = new SubscribeReq() ;
                req.setSubReqID(1001);
                req.setUserName("heartBeat");
                req.setAddress("bjs");
                req.setPhoneNumber("1002");
                req.setPhoneNumber("100xxxx"); 
                ctx.channel().writeAndFlush(req);
            }
        }else{
        	logger.info("应用数据发送....");
            super.userEventTriggered(ctx,evt);
        }
    }

}
