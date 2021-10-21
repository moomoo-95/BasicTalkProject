package moomoo.core.htp.process;

import moomoo.core.htp.util.HtpRequestMessage;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtpOutgoingMessage {

    private static final Logger log = LoggerFactory.getLogger(HtpOutgoingMessage.class);

    /**
     * @fn private boolean outConnect
     * @brief 서버에 Connect 요청 메시지를 전송하는 메서드
     * @return
     */
    public boolean outConnect(){
        String message = HtpRequestMessage.createHtpConnect();
        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }

    /**
     * @fn private boolean outDisconnect
     * @brief 서버에 Disconnect 요청 메시지를 전송하는 메서드
     * @return
     */
    public boolean outDisconnect(){
        String message = HtpRequestMessage.createHtpDisconnect();
        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }

    /**
     * @fn private boolean outEnter
     * @brief 서버에 Enter 요청 메시지를 전송하는 메서드
     * @return
     */
    public boolean outEnter(String conferenceId){
        String message = HtpRequestMessage.createHtpEnter(conferenceId);
        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }

    /**
     * @fn private boolean outExit
     * @brief 서버에 Exit 요청 메시지를 전송하는 메서드
     * @return
     */
    public boolean outExit(){
        String message = HtpRequestMessage.createHtpExit();
        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }

    /**
     * @fn private boolean outMessage
     * @brief 서버에 Message 요청 메시지를 전송하는 메서드
     * @return
     */
    public boolean outMessage(String text) {
        String message = HtpRequestMessage.createHtpMessage(text);
        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }
}
