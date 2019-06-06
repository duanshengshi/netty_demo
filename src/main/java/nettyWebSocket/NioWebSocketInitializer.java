package nettyWebSocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NioWebSocketInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslContext;

    public NioWebSocketInitializer(SslContext ssc){
        this.sslContext = ssc;
    }

    @Override
    protected void initChannel(SocketChannel ch){
        if (sslContext != null) {
            ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
        }
        ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO));//设置log监听器，并且日志级别为debug，方便观察运行流程
        ch.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
        ch.pipeline().addLast(new WebSocketServerCompressionHandler());
        ch.pipeline().addLast("handler",new NioWebSocketHandler());//自定义的业务handler
    }
}
