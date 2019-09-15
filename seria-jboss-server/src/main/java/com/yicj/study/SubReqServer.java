package com.yicj.study;

import com.yicj.study.common.MarshallingCodeCFactory;
import com.yicj.study.handler.SubReqServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
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
					//通过工厂类创建MarshallingEncoder解码器，并添加到ChannelPipeline中
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
					//通过工厂类创建MarshallingEncoder编码器，并添加到ChannelPipeline中
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
					//注意这里的buildMarshallingEncoder必须在SubReqClientHandler前面，
					//否则SubReqClientHandler.channelActive,写的数据无法到达服务端,
					//总结：尽量的将encode放在encoder [outBound]写在encoder前面
					ch.pipeline().addLast(new SubReqServerHandler()) ;
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
