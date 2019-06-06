package nettyWebSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.apache.log4j.Logger;

public class NioWebSocketServer {
    private final Logger logger=Logger.getLogger(this.getClass());


    public void run(int port,boolean SSL) throws Exception{
        final SslContext sslCtx;
        if(SSL){
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }else {
            sslCtx = null;
        }

        logger.info("开始启动websocket服务器");
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NioWebSocketInitializer(sslCtx));
            ChannelFuture future = bootstrap.bind(port).sync();
            if(future.isSuccess()) {
                logger.info("webSocket服务器启动成功："+future.channel());
            }
            future.channel().closeFuture().sync();
        }  finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("websocket服务器已关闭");
        }
    }

    public static void main(String[] args) {
        NioWebSocketServer socketServer = new NioWebSocketServer();
        try {
            socketServer.run(8081,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
