package com.yicj.study.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FixLengthFrameDecoder extends ByteToMessageDecoder {
	
	private final int frameLength ;
	
	public FixLengthFrameDecoder(int frameLength) {
		if(frameLength <= 0) {
			throw new IllegalArgumentException("长度必须为正整数！[frameLength] : " + frameLength) ;
		}
		this.frameLength = frameLength ;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (in.readableBytes() >= frameLength) {
			ByteBuf buf = in.readBytes(frameLength) ;
			out.add(buf) ;
		}
	}
}
