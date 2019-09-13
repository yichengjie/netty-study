package com.yicj.study.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;
@Sharable//标记该类的实例可以被多个Channel共享
public class ClientChannelActiveHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//当被通知Channel是活跃的时候，发送一条信息
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8)) ;
		//注意这里需要手动执行，super.channelActive(ctx);
		//否则，IdleStateHandler无法监听到channelActive事件,将无法正常运行
		super.channelActive(ctx);
	}
}
