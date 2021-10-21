package moomoo.core.htp.process;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.util.HtpRequestMessage;
import moomoo.core.htp.util.HtpResponseMessage;
import moomoo.module.info.UserInfo;
import moomoo.module.manager.UserInfoManager;
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

    /**
     * @fn private boolean inDisconnect
     * @brief DISCONNECT 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    public boolean inDisconnect(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);

        if (userId != null && htpFormat.getBody().containsKey(HtpKey.USER_ID)){
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (!userInfoManager.deleteUserInfo(htpFormat.getBody().get(HtpKey.USER_ID)) ) {
                outDeny(userId, htpFormat, "Do not exist UserInfo");
                return false;
            }

            AppInstance.getInstance().getServerGUI().getUserTextArea().setText(userInfoManager.printUserList());

            outAccept(userId, htpFormat);
            return true;
        } else {
            log.warn("Do not exist userId, userName in REQUEST MESSAGE : {}", htpFormat.getType());
            outDeny(userId, htpFormat, "userId or userName not found");
            return false;
        }
    }

    /**
     * @fn private boolean outAccept
     * @brief 요청 메시지에 대한 승인 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean outAccept(String userId, HtpFormat htpFormat){
        String message = HtpResponseMessage.createHtpAccept(htpFormat);

        NettyManager.getInstance().getNettyClient(userId).send(message);
        return true;
    }

    /**
     * @fn private boolean outDeny
     * @brief 요청 메시지에 대한 거부 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean outDeny(String userId, HtpFormat htpFormat, String reason){
        String message = HtpResponseMessage.createHtpDeny(htpFormat, reason);

        NettyManager.getInstance().getNettyClient(userId).send(message);
        return true;
    }
}
