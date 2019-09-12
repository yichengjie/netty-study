package com.yicj.study.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

public class FrameChunkDecoderTest {

	@Test
	public void testFramesDecodes() {
		
		ByteBuf buf = Unpooled.buffer() ;
		for(int i = 0 ; i < 9 ; i++) {
			buf.writeByte(i) ;
		}
		ByteBuf input = buf.duplicate() ;
		
		//创建一个EmbededChannel，并向其安装一个帧大小为3字节的
		//FixedLengtFrameDecoder
		EmbeddedChannel channel = new EmbeddedChannel(
				new FrameChunkDecoder(3)) ;
		//写入2字节
		assertTrue(channel.writeInbound(input.readBytes(2)));
		//写入4字节
		//如果上面没有抛出异常，那么就会到达这个断言，并且测试失败
		try {
			channel.writeInbound(input.readBytes(4)) ;
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		//写入剩余的3字节,并断言将会产生一个有效的帧
		//读取产生的消息并验证值
		assertTrue(channel.writeInbound(input.readBytes(3)));
		assertTrue(channel.finish());
		
		//Read frames
		ByteBuf read = (ByteBuf)channel.readInbound() ;
		assertEquals(buf.readSlice(2), read);
		read.release() ;
		
		
		read = (ByteBuf) channel.readInbound() ;
		assertEquals(buf.skipBytes(4).readSlice(3), read);
		buf.release() ;
		
	}
}
