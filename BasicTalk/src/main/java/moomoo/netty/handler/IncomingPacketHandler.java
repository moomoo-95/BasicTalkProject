package moomoo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class IncomingPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(IncomingPacketHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        try {
            ByteBuf buf = msg.content();
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);

            if (data.length == 0) {
                return;
            }

            ByteBuffer buffer = ByteBuffer.wrap(data);

            byte[] bodyBuf = new byte[data.length];
            buffer.get(bodyBuf, 0, data.length);
            String body = new String(bodyBuf, Charset.defaultCharset());
            log.debug("Recv Data [{}]  Length [{}]", body, body.length());

        } catch (Exception e) {
            log.error("NettyRedundantHandler.channelRead0.Exception", e);
        }

    }

}