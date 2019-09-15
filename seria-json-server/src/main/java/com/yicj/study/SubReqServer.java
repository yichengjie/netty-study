package com.yicj.study;

import com.yicj.study.codec.JSONDecoder;
import com.yicj.study.codec.JSONEncoder;
import com.yicj.study.handler.SubReqServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

//https://www.cnblogs.com/wade-luffy/p/6169947.html
//https://blog.csdn.net/u012734441/article/details/78769068
public class SubReqServer {
	
	public void bind(int port) throws InterruptedException {
		//配置服务端的Nio线程组
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			ServerBootstrap boot = new ServerBootstrap() ;
			boot.group(group)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new JSONEncoder()) ;
					p.addLast(new JSONDecoder()) ;
					p.addLast(new SubReqServerHandler()) ;
				}
			}) ;
			//绑定端口，同步等待成功
			ChannelFuture f = boot.bind(port).sync();
			//等待服务端监听端口关闭
			f.channel().closeFuture().sync() ;
		} finally {
			group.shutdownGracefully() ;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080 ;
		new SubReqServer().bind(port); 
	}
}
