package com.yicj.study;

import com.yicj.study.common.MarshallingCodeCFactory;
import com.yicj.study.handler.SubReqClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SubReqClient {

	public void connect(String host,int port) throws InterruptedException {
		
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			Bootstrap boot = new Bootstrap() ;
			boot.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
					ch.pipeline().addLast(new SubReqClientHandler()) ;
				}
				
			}); 
			//发起异步连接操作
			ChannelFuture f = boot.connect(host,port).sync();
			//等待客户端链路关闭
			f.channel().closeFuture().sync() ;
		} finally {
			group.shutdownGracefully() ;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080  ;
		String host = "127.0.0.1" ;
		new SubReqClient().connect(host, port);
	}
	
}
