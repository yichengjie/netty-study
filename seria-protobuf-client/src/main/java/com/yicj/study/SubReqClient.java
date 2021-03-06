package com.yicj.study;

import com.yicj.study.handler.SubReqClientHandler;
import com.yicj.study.proto.SubscribeRespProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class SubReqClient {
	
	public void connect(String host,int port) throws InterruptedException {
		
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			Bootstrap boot = new Bootstrap() ;
			boot.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new IdleStateHandler(0,0,5)) ;
					p.addLast(new ProtobufVarint32FrameDecoder()) ;
					p.addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance())) ;
					p.addLast(new ProtobufVarint32LengthFieldPrepender()) ;
					p.addLast(new ProtobufEncoder()) ;
					p.addLast("handler", new SubReqClientHandler()) ;
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
