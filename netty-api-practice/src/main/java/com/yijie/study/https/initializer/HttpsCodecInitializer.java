package com.yijie.study.https.initializer;

import javax.net.ssl.SSLEngine;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

//使用https
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

	private SslContext context ; ;
	private boolean isClient ;
	
	public HttpsCodecInitializer(SslContext context,boolean isClient) {
		this.context = context ;
		this.isClient = isClient ;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		SSLEngine engine = context.newEngine(ch.alloc());
		p.addLast("ssl",new SslHandler(engine)) ;
		if(isClient) {
			p.addLast("codec",new HttpClientCodec()) ;
		}else {
			p.addLast("codec",new HttpServerCodec()) ;
		}
	}
}
