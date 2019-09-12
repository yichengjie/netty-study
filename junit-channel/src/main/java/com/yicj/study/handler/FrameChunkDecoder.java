package com.yicj.study.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

public class FrameChunkDecoder extends ByteToMessageDecoder {
	//指定要产生的帧的最大允许大小
	private final int maxFrameSize ;

	public FrameChunkDecoder(int maxFrameSize) {
		this.maxFrameSize = maxFrameSize ;
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int readableByes = in.readableBytes() ;
		if(readableByes > maxFrameSize) {
			//discard the bytes
			in.clear() ;
			throw new TooLongFrameException() ;
		}
		ByteBuf buf = in.readBytes(readableByes);
		//将该帧添加到解码消息的list中
		out.add(buf) ;
	}

}
