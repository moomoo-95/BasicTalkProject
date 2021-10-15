package moomoo.netty.module;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import moomoo.netty.handler.IncomingPacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @class public class NettyServer
 * @brief netty를 활용하여 udp 를 받는 클래스
 * @author hyeonseong Lim
 */
public class NettyServer {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private final int threadSize;
    private final int port;

    private Bootstrap b;
    private NioEventLoopGroup group;
    private ChannelFuture channelFuture;

    public NettyServer(int threadSize, int port) {
        this.threadSize = threadSize;
        this.port = port;
    }

    public NettyServer run() {
        group = new NioEventLoopGroup(threadSize);

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
                    .localAddress(new InetSocketAddress(port))
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        public void initChannel(final NioDatagramChannel ch) {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IncomingPacketHandler());
                        }
                    });

            openChannel();
        } catch (Exception e) {
            log.error("NettyServer.run.Exception ", e);
        } finally {
            log.info("In Server Finally");
        }
        return null;
    }

    private void openChannel() {
        try {
            channelFuture = b.bind().sync();
        } catch (InterruptedException e) {
            log.error("channel open Interrupted! socket=localhost:{} ", port, e);
            Thread.currentThread().interrupt();
        }
    }

    private void closeChannel() {
        if (channelFuture != null) {
            channelFuture.channel().closeFuture();
            channelFuture.channel().close();
        }
    }

    public void close() {
        closeChannel();
        group.shutdownGracefully();
    }

}
