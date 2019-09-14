package com.yicj.study;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UDPClient {
	
	public static void main(String[] args) {
		
	}

	public void start() {
		OioEventLoopGroup grop = new OioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap() ;
		bootstrap.group(grop)
		.channel(OioDatagramChannel.class)
		.handler(new MyChannelHandler()) ;
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture fu) throws Exception {
				if(future.isSuccess()) {
					log.info("Channel bound");
				}else {
					Throwable cause = fu.cause();
					log.error("Bind attempt failed",cause);
				}
			}
		}) ;
		Future<?> shutdownGracefully = grop.shutdownGracefully();
		shutdownGracefully.syncUninterruptibly() ;
	}
	
	class MyChannelHandler extends SimpleChannelInboundHandler<DatagramPacket>{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
			// Do something with the packet
		}
	}
	
}
