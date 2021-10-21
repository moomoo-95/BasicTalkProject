package moomoo.core.htp.process;

import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.util.HtpResponseMessage;
import moomoo.netty.NettyManager;

public class HtpIncomingMessage {

    /**
     * @fn private boolean outAccept
     * @brief 요청 메시지에 대한 승인 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean outAccept(HtpFormat htpFormat){
        String message = HtpResponseMessage.createHtpAccept(htpFormat);

        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }

    /**
     * @fn private boolean outDeny
     * @brief 요청 메시지에 대한 거부 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean outDeny(HtpFormat htpFormat, String reason){
        String message = HtpResponseMessage.createHtpDeny(htpFormat, reason);

        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }
}
