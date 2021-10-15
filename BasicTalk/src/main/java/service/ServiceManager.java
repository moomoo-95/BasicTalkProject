package service;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.nettyClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @class public class ServiceManager
 * @brief BasicTalk Server 의 Service 를 관리 하는 클래스
 * @author hyeonseong Lim
 */
public class ServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    private final String host;
    private final int port;

    public ServiceManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        // 새 연결 생성, 인/아웃바운드 데이터 처리를 포함하는 이벤트 처리를 제어할 인스턴스(NioEventLoopGroup) 만들고 할당
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 클라이언트를 초기화하기 위해 생성
            Bootstrap bootstrap = new Bootstrap();
            // 1) 클라이언트 이벤트 처리를 위해 EventLoopGroup 지정 (NIO 구현)
            bootstrap.group(eventLoopGroup)
                    // 2) 채널 유형 지정(NIO 전송 유형)
                    .channel(NioSocketChannel.class)
                    // 3) 서버 주소 설정
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 4) 채널 생성 시 마다 파이프라인에 EchoClientHandler 하나를 추가
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new nettyClientHandler());
                        }
                    });
            // 5) 원격 피어로 연결하고 연결 완료되기 기다림
            ChannelFuture channelFuture = bootstrap.connect().sync();
            // 6) 채널 닫힐 때 까지 블로킹
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            logger.error("EchoClientManager.start.Exception", e);
        }

        try {
            // 6) 스레드 풀 종료하고 모든 리소스 해제
            eventLoopGroup.shutdownGracefully().sync();
        } catch ( Exception e){
            logger.error("EchoClientManager.start.shutdownGracefully.Exception", e);
        }

    }
}
