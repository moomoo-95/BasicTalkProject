package service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @class public class ServiceManager
 * @brief BasicTalk Server 의 Service 를 관리 하는 클래스
 * @author hyeonseong Lim
 */
public class ServiceManager {

    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private final int port;

    public ServiceManager(int port) {
        this.port = port;
    }

    public void start() {
        final ServerHandler serverHandler = new ServerHandler();
        // 새 연결 수락 및 데이터 읽기/쓰기와 같은 이벤트 처리 수행
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            // 서버를 부트스트랩하고 바인딩하는데 이용
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 1) NIO 전송 채널 지정, 포트를 통해 소켓 주소 설정, EchoServerHandler 하나를 채널의 Channel Pipeline으로 추가
            serverBootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // 2) EchoServerHandler가 Sharable이므로 동일 항목 이용 가능
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });

            // 3) 서버를 비동기식으로 바인딩, 완료대기
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            // 4) 채널의 CloseFuture를 얻고 완료될 때까지 현재 스레드를 블로킹
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("ServiceManager.start.Exception", e);
        }

        try {
            // 5) EventLoopGroup을 종료하고 모든 리소스 해제
            eventLoopGroup.shutdownGracefully().sync();
        } catch ( Exception e){
            log.error("ServiceManager.start.shutdownGracefully.Exception", e);
        }
    }
}
