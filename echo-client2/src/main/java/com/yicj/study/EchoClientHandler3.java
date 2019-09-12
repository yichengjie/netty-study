package com.yicj.study;

import java.util.List;

import com.yicj.study.vo.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoClientHandler3 extends MessageToMessageDecoder<Message>{
	
	@Override
	protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		log.info("EchoClientHandler3 decode ... " + msg.toString());
		out.add(msg) ;
	}

}
