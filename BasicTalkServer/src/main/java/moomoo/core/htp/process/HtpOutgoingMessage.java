package moomoo.core.htp.process;

import moomoo.core.htp.util.HtpRequestMessage;
import moomoo.module.info.UserInfo;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtpOutgoingMessage {

    private static final Logger log = LoggerFactory.getLogger(HtpOutgoingMessage.class);

    /**
     * @fn private boolean outMessage
     * @brief MESSAGE 메시지 송신시 처리하는 메서드
     * @return
     */
    public boolean outMessage(UserInfo userInfo, String text) {
        String userId = userInfo.getUserId();

        String message = HtpRequestMessage.createHtpMessage(userInfo.getUserIp(), userInfo.getUserPort(), userInfo.getTransactionSeq(), userId, userInfo.getConferenceId(), text);
        NettyManager.getInstance().getNettyClient(userId).send(message);
        return true;
    }

}
