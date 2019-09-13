package com.yicj.study.initializer;

import java.io.File;
import java.io.FileInputStream;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel>{
	
	private File file ;
	private SslContext sslCtx ;
	
	public ChunkedWriteHandlerInitializer(File file,SslContext sslCtx) {
		this.file = file ;
		this.sslCtx = sslCtx ;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		SSLEngine sslEngine = sslCtx.newEngine(ch.alloc());
		//将SslHandler添加到ChannelPipeline 
		pipeline.addLast(new SslHandler(sslEngine)) ;
		//添加ChunkedWriteHandler以处理作为WriteStreamHandler传入的数据
		pipeline.addLast(new ChunkedWriteHandler()) ;
		pipeline.addLast(new WriteStreamHandler()) ;
	}
	
	public class WriteStreamHandler extends ChannelInboundHandlerAdapter{
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			//当连接建立时，chanelActive()方法将使用ChunckedInput写文件数据
			ChunkedStream chunkedStream = new ChunkedStream(new FileInputStream(file));
			ctx.writeAndFlush(chunkedStream) ;
		}
	}

}
