package com.yicj.study.codec;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONDecoder extends LengthFieldBasedFrameDecoder {

    public JSONDecoder() {
        super(65535, 0, 4,0,4);
    }
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    	log.info("JSONEncoder decode method exec .....");
    	ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode==null){
            return null;
        }
        int data_len = decode.readableBytes();
        byte[] bytes = new byte[data_len];
        decode.readBytes(bytes);
        Object parse = JSON.parse(bytes);
        return parse;
    }
}