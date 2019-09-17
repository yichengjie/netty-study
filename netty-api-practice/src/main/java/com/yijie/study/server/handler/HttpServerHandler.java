package com.yijie.study.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof HttpContent) {
			LastHttpContent httpContent = (LastHttpContent) msg;
			ByteBuf byteData = httpContent.content();
			if (byteData instanceof EmptyByteBuf) {
				System.out.println("Content:无数据");
			} else {
				String content = convertByteBufToString(byteData) ;
				System.out.println("Content: " + content);
			}
		}

		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.wrappedBuffer("欢迎来到北京动物园".getBytes("utf-8")));
		resp.headers().set(Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
		resp.headers().set(Names.CONTENT_LENGTH, resp.content().readableBytes());
		resp.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
		ctx.write(resp);
		ctx.flush();
	}

	public String convertByteBufToString(ByteBuf buf) {
		String str;
		if (buf.hasArray()) { // 处理堆缓冲区
			str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
		} else { // 处理直接缓冲区以及复合缓冲区
			byte[] bytes = new byte[buf.readableBytes()];
			buf.getBytes(buf.readerIndex(), bytes);
			str = new String(bytes, 0, buf.readableBytes());
		}
		return str;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace();
	}

}
