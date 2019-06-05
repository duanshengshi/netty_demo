package nettyWebSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

public class NioWebSocketServer {
    private final Logger logger=Logger.getLogger(this.getClass());
    public void run(int port){
        logger.info("开始启动websocket服务器");
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NioWebSocketInitializer());
            ChannelFuture future = bootstrap.bind(port).sync();
            if(future.isSuccess()) {
                logger.info("webSocket服务器启动成功："+future.channel());
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("运行出错："+e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("websocket服务器已关闭");
        }
    }

    public static void main(String[] args) {
        NioWebSocketServer socketServer = new NioWebSocketServer();
        socketServer.run(8081);
    }
}
