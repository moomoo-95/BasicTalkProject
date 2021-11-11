package moomoo.core.htp.process;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.base.HtpType;
import moomoo.core.htp.util.HtpResponseMessage;
import moomoo.module.info.ConferenceInfo;
import moomoo.module.info.UserInfo;
import moomoo.module.manager.ConferenceInfoManager;
import moomoo.module.manager.UserInfoManager;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class HtpIncomingMessage {

    private static final Logger log = LoggerFactory.getLogger(HtpIncomingMessage.class);
    private static final HtpOutgoingMessage htpOutgoingMessage = new HtpOutgoingMessage();

    /**
     * @fn private boolean inConnect
     * @brief CONNECT 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    public boolean inConnect(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);

        if (userId != null && htpFormat.getBody().containsKey(HtpKey.USER_NAME)){
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (userInfoManager.createUserInfo(htpFormat.getBody().get(HtpKey.USER_ID), htpFormat.getFromIp(), htpFormat.getBody().get(HtpKey.USER_NAME), htpFormat.getFromPort(), htpFormat.getTransaction()) == null){
                outDeny(userId, htpFormat, "UserInfo is not created. Please Retry CONNECT");
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
     * @fn private boolean inDisconnect
     * @brief DISCONNECT 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    public boolean inDisconnect(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);

        if (userId != null){
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (!userInfoManager.deleteUserInfo(htpFormat.getBody().get(HtpKey.USER_ID)) ) {
                outDeny(userId, htpFormat, "Do not exist UserInfo");
                return false;
            }

            AppInstance.getInstance().getServerGUI().getUserTextArea().setText(userInfoManager.printUserList());

            outAccept(userId, htpFormat);
            return true;
        } else {
            log.warn("Do not exist userId in REQUEST MESSAGE : {}", htpFormat.getType());
            outDeny(userId, htpFormat, "userId or userName not found");
            return false;
        }
    }

    /**
     * @fn private boolean inEnter
     * @brief ENTER 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    public boolean inEnter(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);
        String conferenceId = htpFormat.getBody().get(HtpKey.CONFERENCE_ID);

        if (userId != null && conferenceId != null){
            ConferenceInfoManager conferenceInfoManager = ConferenceInfoManager.getInstance();
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (!userInfoManager.getUserInfoMap().containsKey(userId)) {
                outDeny(userId, htpFormat, "Do you not Connected UserInfo");
                return false;
            }
            if (conferenceInfoManager.getConferenceInfoMap().containsKey(conferenceId) ) {
                conferenceInfoManager.getConferenceInfo(conferenceId).addUserSet(userId);
            } else {
                conferenceInfoManager.createConferenceInfo(conferenceId, userId);
            }

            AppInstance.getInstance().getServerGUI().getConferenceTextArea().setText(conferenceInfoManager.printConferenceList());
            userInfoManager.getUserInfo(userId).setConferenceId(conferenceId);

            outAccept(userId, htpFormat);
            userInfoManager.getUserInfoMap().forEach((key, userInfo) -> htpOutgoingMessage.outMessage(HtpKey.CONFERENCE_ID, userInfo, conferenceInfoManager.printConferenceList()));
            return true;
        } else {
            log.warn("Do not exist userId, conferenceId in REQUEST MESSAGE : {}", htpFormat.getType());
            outDeny(userId, htpFormat, "userId or userName not found");
            return false;
        }
    }

    /**
     * @fn private boolean inExit
     * @brief EXIT 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    public boolean inExit(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);
        String conferenceId = htpFormat.getBody().get(HtpKey.CONFERENCE_ID);

        if (userId != null && conferenceId != null){
            ConferenceInfoManager conferenceInfoManager = ConferenceInfoManager.getInstance();
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (!userInfoManager.getUserInfoMap().containsKey(userId)) {
                outDeny(userId, htpFormat, "Do you not Connected UserInfo");
                return false;
            }
            if (conferenceInfoManager.getConferenceInfoMap().containsKey(conferenceId) ) {
                ConferenceInfo conferenceInfo = conferenceInfoManager.getConferenceInfo(conferenceId);
                conferenceInfo.removeUserSet(userId);
                if (conferenceInfo.isEmpty()) {
                    conferenceInfo.clear();
                    conferenceInfoManager.deleteConferenceInfo(conferenceId);
                }
            } else {
                outDeny(userId, htpFormat, "Do not exist ConferenceInfo");
                return false;
            }

            AppInstance.getInstance().getServerGUI().getConferenceTextArea().setText(conferenceInfoManager.printConferenceList());
            userInfoManager.getUserInfo(userId).setConferenceId("");

            outAccept(userId, htpFormat);
            userInfoManager.getUserInfoMap().forEach((key, userInfo) -> htpOutgoingMessage.outMessage(HtpKey.CONFERENCE_ID, userInfo, conferenceInfoManager.printConferenceList()));
            return true;
        } else {
            log.warn("Do not exist userId, conferenceId in REQUEST MESSAGE : {}", htpFormat.getType());
            outDeny(userId, htpFormat, "userId or userName not found");
            return false;
        }
    }

    /**
     * @fn private boolean inMessage
     * @brief MESSAGE 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    public boolean inMessage(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);
        String conferenceId = htpFormat.getBody().get(HtpKey.CONFERENCE_ID);
        log.debug("recvmsg => userId : {}, conferenceId : {}", userId, conferenceId);
        if (userId != null && conferenceId != null){
            ConferenceInfoManager conferenceInfoManager = ConferenceInfoManager.getInstance();
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (!userInfoManager.getUserInfoMap().containsKey(userId)) {
                outDeny(userId, htpFormat, "Do you not Connected UserInfo");
                return false;
            }
            if (!conferenceInfoManager.getConferenceInfoMap().containsKey(conferenceId)) {
                outDeny(userId, htpFormat, "Do not exist Conference room");
                return false;
            }

            ConferenceInfo conferenceInfo = conferenceInfoManager.getConferenceInfo(conferenceId);

            String text = htpFormat.getBody().get(HtpKey.TEXT);
            conferenceInfo.getUserSet().forEach( value -> htpOutgoingMessage.outMessage(HtpType.MESSAGE, userInfoManager.getUserInfo(value), text) );
            return true;
        } else {
            log.warn("Do not exist userId, conferenceId in REQUEST MESSAGE : {}", htpFormat.getType());
            outDeny(userId, htpFormat, "userId or conferenceId not found");
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

        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo(userId);
        if (userInfo != null) {
            userInfo.setTransactionSeq(htpFormat.getTransaction());
        }
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

        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo(userId);
        if (userInfo != null) {
            userInfo.setTransactionSeq(htpFormat.getTransaction());
        }
        NettyManager.getInstance().getNettyClient(userId).send(message);
        return true;
    }
}
