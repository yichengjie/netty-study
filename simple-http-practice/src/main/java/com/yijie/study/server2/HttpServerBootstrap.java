package com.yijie.study.server2;
import com.yijie.study.server2.handler.HttpServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServerBootstrap {
	
	public static void main(String[] args) throws InterruptedException {
		int port = 2222 ;
		new HttpServerBootstrap().run(port) ;
	}

	private void run(int port) throws InterruptedException {
		EventLoopGroup boosGroup = new NioEventLoopGroup() ;
		EventLoopGroup workGroup = new NioEventLoopGroup() ;
		ServerBootstrap boot = new ServerBootstrap() ;
		boot.group(boosGroup,workGroup) ;
		boot.channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new HttpResponseEncoder()) ;
				p.addLast(new HttpRequestDecoder()) ;
				//http压缩
				p.addLast(new HttpContentCompressor()) ;
				//聚合http消息
				p.addLast(new HttpObjectAggregator(512* 1024)) ;
				p.addLast(new HttpServerHandler()) ;
				
			}
		})
		.option(ChannelOption.SO_BACKLOG, 128)
		.childOption(ChannelOption.SO_KEEPALIVE, true) ;
		try {
			ChannelFuture f = boot.bind(port).sync();
			f.channel().closeFuture().sync() ;
		} finally {
			workGroup.shutdownGracefully() ;
			boosGroup.shutdownGracefully() ;
		}
		
	}
}
