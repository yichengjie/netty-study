package com.yicj.study;

import com.yicj.study.codec.MarshallingCodeCFactory;
import com.yicj.study.handler.SubReqServerHandler2;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

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
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.TCP_NODELAY, true)
			//.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new IdleStateHandler(0, 0, 20));
					//p.addLast(new JSONEncoder()) ;
					//p.addLast(new JSONDecoder(SubscribeReq.class)) ;
					p.addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
					p.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
					p.addLast(new SubReqServerHandler2()) ;
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
