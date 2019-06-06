package nettyWebSocket.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.log4j.Logger;

import java.util.Locale;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.CANADA)));
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
////         ctx.fireChannelActive();
//        String channelId = ctx.channel().id().asLongText();
//        logger.info("客户端连接:" + channelId);
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        String channelId = ctx.channel().id().asLongText();
//        logger.info("websocket channel inactive: " + channelId);
//
////              ctx.fireChannelInactive();
//    }
}
