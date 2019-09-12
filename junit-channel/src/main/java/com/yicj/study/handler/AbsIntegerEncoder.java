package com.yicj.study.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//检查是否有足够的字节用来编码
		while(in.readableBytes() >= 4) {
			//从ByteBuf中读取下一个整数，并计算绝对值
			int value = Math.abs(in.readInt()) ;
			//将整数写入到编码消息的list中
			out.add(value) ;
		}
	}
}
