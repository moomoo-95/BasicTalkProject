package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class public class EchoClientHandler
 * @brief 서버로 부터 받은 데이터를 클라이언트측에서 처리하는 비즈니스 논리를 구현한 핸들러 클래스
 * @author hyeonseong Lim
 */
// 이 클래스의 인스턴스를 여러 채널에서 공유할 수 있음을 나타냄
@Sharable
public class nettyClientHandler  extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(nettyClientHandler.class);

    public nettyClientHandler() {
        // Nothing
    }

    /**
     * @fn public void channelActive
     * @brief 서버에 대한 연결이 생성되면 호출되는 메서드
     * @param context
     */
    @Override
    public void channelActive(ChannelHandlerContext context){

        // 채널 활성화 알림을 받으면 메시지 전송
        context.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    /**
     * @fn protected void channelRead0
     * @brief 서버로부터 메시지를 수신하면 호출되는 메서드
     * @param context
     * @param byteBuf
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf byteBuf) {
        logger.debug("Client received : {}", byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * @fn public void exceptionCaught
     * @brief 예외사항 발생시 예외스택을 출력하고 채널을 닫는 메서드
     * @param context 채널
     * @param cause 예외사항
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause){
        logger.error("EchoServerHandler.exceptionCaught.Exception", cause);
        context.close();
    }
}
