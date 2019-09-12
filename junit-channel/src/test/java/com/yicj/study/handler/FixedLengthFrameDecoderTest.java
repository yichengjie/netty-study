package com.yicj.study.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

public class FixedLengthFrameDecoderTest {
	
	@Test
	public void testFramesDecoded() {
		ByteBuf buf = Unpooled.buffer() ;
		for(int i = 0 ; i < 9 ; i ++) {
			buf.writeByte(i) ;
		}
		
		
		int frameLength = 3 ;
		ByteBuf input = buf.duplicate() ;
		EmbeddedChannel channel = new EmbeddedChannel(
				new FixedLengthFrameDecoder(frameLength)) ;
		//将1-9全部写入写入
		//write bytes
		assertTrue(channel.writeInbound(input.retain()));
		assertTrue(channel.finish());
		//read message
		ByteBuf read = (ByteBuf)channel.readInbound() ;
		assertEquals(buf.readSlice(frameLength), read);
		printByteBuf(read);
		System.out.println("==============================");
		read.release() ;
		
		read =  (ByteBuf)channel.readInbound() ;
		assertEquals(buf.readSlice(frameLength), read);
		printByteBuf(read);
		System.out.println("==============================");
		read.release() ;
		
		read = (ByteBuf)channel.readInbound() ;
		assertEquals(buf.readSlice(frameLength), read);
		printByteBuf(read);
		read.release() ;
		System.out.println("==============================");
		
		assertNull(channel.readInbound());
		buf.release() ;
		
	}
	
	@Test
	public void testFramesDecoded2() {
		ByteBuf buf = Unpooled.buffer() ;
		for(int i = 0 ; i < 9 ; i ++) {
			buf.writeByte(i) ;
		}
		
		
		int frameLength = 3 ;
		ByteBuf input = buf.duplicate() ;
		EmbeddedChannel channel = new EmbeddedChannel(
				new FixedLengthFrameDecoder(frameLength)) ;
		//将1-9全部写入写入
		//write bytes  ,这里将会返回false
		assertTrue(channel.writeInbound(input.readBytes(2)));
		assertTrue(channel.writeInbound(input.readBytes(7)));
		//
		assertTrue(channel.finish());
		//read message
		ByteBuf read = (ByteBuf)channel.readInbound() ;
		assertEquals(buf.readSlice(frameLength), read);
		printByteBuf(read);
		System.out.println("==============================");
		read.release() ;
		
		read =  (ByteBuf)channel.readInbound() ;
		assertEquals(buf.readSlice(frameLength), read);
		printByteBuf(read);
		System.out.println("==============================");
		read.release() ;
		
		read = (ByteBuf)channel.readInbound() ;
		assertEquals(buf.readSlice(frameLength), read);
		printByteBuf(read);
		read.release() ;
		System.out.println("==============================");
		
		assertNull(channel.readInbound());
		buf.release() ;
		
	}
	
	public void printByteBuf(ByteBuf buf) {
		List<String> list = new ArrayList<>() ;
		while(buf.isReadable()) {
			byte readByte = buf.readByte(); 
			list.add(readByte + "") ;
		}
		System.out.println(list.toString());
	}

}
