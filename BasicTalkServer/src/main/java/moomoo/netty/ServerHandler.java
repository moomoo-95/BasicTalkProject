package moomoo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class public class ServerHandler
 * @brief 클라이언트로부터 받은 데이터를 서버측에서 처리하는 비즈니스 논리를 구현한 핸들러 클래스
 * @author hyeonseong Lim
 */
// 여러 채널 간 안전하게 공유할 수 있음을 나타냄
@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    public ServerHandler() {
        // Nothing
    }

    /**
     * @fn public void channelRead
     * @brief 메시지가 들어올 때마다 호출되는 메서드
     * @param context 채널
     * @param message 인바운드된 메시지
     */
    @Override
    public void channelRead(ChannelHandlerContext context, Object message){
        ByteBuf inboundMessage = (ByteBuf) message;
        logger.debug("Server received : {}", inboundMessage.toString(CharsetUtil.UTF_8));

        // 아웃바운드 메시지를 플러시하지 않은 채로 받은 메시지를 발신자로 출력
        context.write(inboundMessage);
    }

    /**
     * @fn public void channelReadComplete
     * @brief channelRead의 마지막 호출에서 현재 일괄 처리의 마지막 메시지를 처리했음을 핸들러에게 통보하는 메서드
     * @param context 채널
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        // 대기 중인 메시지를 원격 피어로 플러시하고 채널을 닫음
        context.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * @fn public void exceptionCaught
     * @brief 읽기 작업 중 예외사항 발생시 예외스택을 출력하고 채널을 닫는 메서드
     * @param context 채널
     * @param cause 예외사항
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        logger.error("EchoServerHandler.exceptionCaught.Exception", cause);
        context.close();
    }
}
