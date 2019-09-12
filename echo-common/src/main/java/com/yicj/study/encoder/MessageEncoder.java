package com.yicj.study.encoder;

import com.yicj.study.decoder.MessageDecoder;
import com.yicj.study.vo.Header;
import com.yicj.study.vo.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<Message> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		log.info("MessageEncoder exec .....");
		Header header = msg.getHeader();
		out.writeByte(MessageDecoder.PACKAGE_TAG);
		out.writeByte(header.getEncode());
		out.writeByte(header.getEncrypt());
		out.writeByte(header.getExtend1());
		out.writeByte(header.getExtend2());
		out.writeBytes(header.getSessionid().getBytes());
		out.writeInt(header.getLength());
		out.writeInt(header.getCammand());
		out.writeBytes(msg.getData().getBytes("UTF-8"));
	}
}
