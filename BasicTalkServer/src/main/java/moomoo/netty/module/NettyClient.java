package moomoo.netty.module;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import moomoo.netty.handler.OutgoingPacketHandler;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.ThreadFactory;

/**
 * @class public class NettyClient
 * @brief netty를 활용하여 udp 를 보내는 클래스
 * @author hyeonseong Lim
 */
public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private final InetSocketAddress socketAddress;
    private final int threadSize;
    private final String ip;
    private final int port;

    private Bootstrap b;
    private NioEventLoopGroup group;
    private ChannelFuture channelFuture;

    public NettyClient(int threadSize, String ip, int port) {
        this.socketAddress = new InetSocketAddress(ip, port);
        this.threadSize = threadSize;
        this.ip = ip;
        this.port = port;
    }

    public NettyClient run(){
        ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("NettyClient-NioEventLoop-%d")
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .build();
        group = new NioEventLoopGroup(threadSize, threadFactory);

        try {
            int bufferSize = 32 * 1024 * 1024;

            b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, false)
                    .option(ChannelOption.SO_SNDBUF, bufferSize)
                    .option(ChannelOption.SO_RCVBUF, bufferSize)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .remoteAddress(socketAddress)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        public void initChannel(final NioDatagramChannel ch){
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new OutgoingPacketHandler());
                        }
                    });
            connectChannel();
        } catch (Exception e) {
            log.error("NettyClient.run.Exception ", e);
        } finally {
            log.info("In Client Finally");
        }
        return null;
    }

    public void close() {
        closeChannel();
        group.shutdownGracefully();
    }

    private void connectChannel() {
        try {
            channelFuture = b.connect().sync();
        } catch (InterruptedException e) {
            log.error("channel connect Interrupted! socket={}:{} ", ip, port, e);
            Thread.currentThread().interrupt();
        }
    }

    private void closeChannel() {
        if (channelFuture.channel() != null) {
            channelFuture.channel().disconnect();
            channelFuture.channel().closeFuture();
            channelFuture.channel().close();
        }
    }

    public void send(int msgType, String body) {
        int bufSize = body.length() + 4;
        ByteBuffer buf = ByteBuffer.allocate(bufSize);
        buf.put((byte) msgType);
        buf.putShort((short) body.length());
        buf.put(body.getBytes(Charset.defaultCharset()));
        log.debug("Send Data [{}] Length (header/body) [{}/{}], [{}]", body, buf.capacity() - body.length(), body.length(), msgType);

        byte[] msg = new byte[buf.position()];

        buf.rewind();
        buf.get(msg);

        ByteBuf byteBuf = Unpooled.copiedBuffer(msg);
        channelFuture.channel().writeAndFlush(new DatagramPacket(byteBuf, socketAddress));
    }
}
