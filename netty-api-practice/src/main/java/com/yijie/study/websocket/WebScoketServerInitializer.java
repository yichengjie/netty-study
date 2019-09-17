package com.yijie.study.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class WebScoketServerInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpServerCodec()) ;
		p.addLast(new HttpObjectAggregator(65536)) ;
		p.addLast(new WebSocketServerProtocolHandler("/websocket")) ;
		p.addLast(new TextFrameHandler()) ;
		p.addLast(new BinaryFrameHandler()) ;
		p.addLast(new ContinuationFrameHandler()) ;
	}
	
	public class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
			//handler text frame
		}
	}
	
	public class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
			//handler binary frame
		}
	}
	
	public class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame>{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
			// handler continuation frame
		}
	}

}
