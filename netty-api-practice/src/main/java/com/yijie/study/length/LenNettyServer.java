package com.yijie.study.length;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class LenNettyServer {

	public static void main(String[] args) throws InterruptedException {
		int port = 8000 ;
		new LenNettyServer().start(port); 
	}

	public void start(int port) throws InterruptedException {
		// 申明服务类
		ServerBootstrap boot = new ServerBootstrap();
		// 设定线程池
		EventLoopGroup group = new NioEventLoopGroup();
		boot.group(group)
		.channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new LengthFieldBasedFrameDecoder(64 * 1024, 0, 8));
				p.addLast(new FrameHandler());
			}
		});
		try {
			ChannelFuture f = boot.bind(port).sync();
			f.channel().closeFuture().sync() ;
		} finally {
			group.shutdownGracefully() ;
		}
	}

	public class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
			// do something with the frame
		}
	}

}
