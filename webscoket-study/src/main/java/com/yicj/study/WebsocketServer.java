package com.yicj.study;

import com.yicj.study.handler.MyWebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebsocketServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080 ;
        //配置服务端的Nio线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup() ;
        EventLoopGroup workGroup = new NioEventLoopGroup() ;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap() ;
            bootstrap.group(bossGroup,workGroup) ;
            bootstrap.channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("http-codec",new HttpServerCodec()) ;
                    pipeline.addLast("aggregator",new HttpObjectAggregator(65536)) ;
                    pipeline.addLast("http-chunked",new ChunkedWriteHandler()) ;
                    pipeline.addLast("handler", new MyWebSocketHandler()) ;
                }
            }) ;
            //绑定端口，同步等待成功
            ChannelFuture f = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync() ;
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully() ;
        }

    }
}
