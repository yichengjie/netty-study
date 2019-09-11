package com.yicj.study;

import java.net.InetSocketAddress;

import com.yicj.study.decoder.MessageDecoder;
import com.yicj.study.encoder.MessageEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	private final int port ;
	public EchoServer(int port) {
		this.port = port ;
	}
	
	public static void main(String[] args) throws InterruptedException {
		EchoServer server = new EchoServer(3003) ;
		server.start(); 
	}
	
	
	public void start() throws InterruptedException {
		final EchoServerHandler serverHandler = new EchoServerHandler() ;
		//创建EventLoopGroup
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			//创建ServerBootstrap
			ServerBootstrap b = new ServerBootstrap() ;
			b.group(group)
			.channel(NioServerSocketChannel.class)
			//使用指定的端口设置套接字地址
			.localAddress(new InetSocketAddress(port))
			//添加一个EchoServerHandler到子Channel的ChannelPipeline
			//EchoSeverHandler被标记为@Shareable，所以我们可以总是使用同样的实例
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new MessageDecoder()) ;
					p.addLast(new MessageEncoder()) ;
					p.addLast(serverHandler) ;
				}
			}) ;
			//异步的绑定服务器，调用sync阻塞等待，直到绑定完成
			ChannelFuture f = b.bind().sync() ;
			//获取Channel的CloseFuture，并且阻塞当前线程直到它完成
			f.channel().closeFuture().sync() ;
		} finally {
			//关闭EventLoopGroup，释放所有的资源
			group.shutdownGracefully().sync() ;
		}
	}

}
