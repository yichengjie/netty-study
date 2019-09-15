package com.yicj.study.codec;

import java.util.List;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONDecoder extends ByteToMessageDecoder {
	private Class<?> clazz ;
	public JSONDecoder(Class<?> clazz) {
		this.clazz = clazz ;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		log.info("JSONEncoder decode method exec .....");
		int len = in.readableBytes();
		/*
		 * byte[] bytes = new byte[dataLen]; in.readBytes(bytes); Object parse =
		 * JSON.parse(bytes); out.add(parse) ;
		 */
		byte data[] = new byte[len];
		in.getBytes(in.readerIndex(), data, 0, len);
		out.add(JSON.parseObject(new String(data)).toJavaObject(clazz));
	}

//    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//    	log.info("JSONEncoder decode method exec .....");
//    	ByteBuf decode = (ByteBuf) super.decode(ctx, in);
//        if (decode==null){
//            return null;
//        }
//        int data_len = decode.readableBytes();
//        byte[] bytes = new byte[data_len];
//        decode.readBytes(bytes);
//        Object parse = JSON.parse(bytes);
//        return parse;
//    }
}