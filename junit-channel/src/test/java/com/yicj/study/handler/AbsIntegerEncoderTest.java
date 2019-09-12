package com.yicj.study.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

public class AbsIntegerEncoderTest {

	@Test
	public void testEncoder() {

		ByteBuf buf = Unpooled.buffer();
		for(int i =0 ; i < 10 ; i++) {
			int tmp = (i * -1) ;
			buf.writeInt(tmp) ;
		}
		EmbeddedChannel channel = new EmbeddedChannel(
				new AbsIntegerEncoder()) ;
		
		assertTrue(channel.writeOutbound(buf));
		assertTrue(channel.finish());
		
		//read bytes 
		for(int i = 0 ; i < 10 ; i ++) {
			Object outBound = channel.readOutbound();
			assertEquals(i, outBound);
		}
		assertNull(channel.readOutbound());
	}
}
