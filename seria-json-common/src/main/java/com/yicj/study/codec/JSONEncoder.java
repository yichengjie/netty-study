package com.yicj.study.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONEncoder extends MessageToByteEncoder<ByteBuf> {@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		log.info("JSONEncoder encode method exec .....");
		byte data [] = JSONObject.toJSONString(msg).getBytes() ;
	    out.writeBytes(data) ;
	}
//	@Override
//	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
//		log.info("JSONEncoder encode method exec .....");
//		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
//        byte[] bytes = JSON.toJSONBytes(msg);
//        byteBuf.writeInt(bytes.length);
//        byteBuf.writeBytes(bytes);
//        out.add(byteBuf);
//	}
}