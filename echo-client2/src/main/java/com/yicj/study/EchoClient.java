package com.yicj.study;

import java.net.InetSocketAddress;

import com.yicj.study.decoder.MessageDecoder;
import com.yicj.study.encoder.MessageEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
	private final String host ;
	private final int port ;
	
	public EchoClient(String host, int port) {
		this.host = host ;
		this.port = port ;
	}

	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			//创建bootstrap
			Bootstrap b = new Bootstrap() ;
			//指定EventLoopGroup以处理客户端时间，需要适用于NIO的实现
			b.group(group)
			//适用于NIO传输的Channel类型
			.channel(NioSocketChannel.class)
			//设置服务器的InetSocketAddress
			.remoteAddress(new InetSocketAddress(host,port))
			//创建Channel时，向ChannelPipeline中添加一个EchoClientHandler实例
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new MessageEncoder()) ;//outBound
					//1.这里需要先使用MessageDecoder解码
					p.addLast(new MessageDecoder()) ; //inBound
					//2.待MessageDecoder解码后才能使用EchoClientHandler直接读取数据
					p.addLast(new EchoClientHandler()) ;//inBound
					p.addLast(new EchoClientHandler2()) ;//inBound
				}
			}) ;
			//连接到远程节点，阻塞等待直到连接完成
			ChannelFuture f = b.connect().sync() ;
			//阻塞，直到Channel关闭
			f.channel().closeFuture().sync() ;
		} finally {
			//关闭线程池并且四方所有的资源
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1" ;
		int port = 3003 ;
		EchoClient client = new EchoClient(host, port) ;
		client.start();
	}
}
