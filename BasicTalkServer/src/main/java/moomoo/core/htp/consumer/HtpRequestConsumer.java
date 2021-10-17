package moomoo.core.htp.consumer;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.base.HtpType;
import moomoo.core.htp.util.HtpCreateMessage;
import moomoo.core.htp.util.HtpParser;
import moomoo.module.manager.UserInfoManager;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class public class HtpRequestConsumer
 * @brief HTP 요청 메시지를 처리하는 클래스
 * @author hyeonseong Lim
 */
public class HtpRequestConsumer {
    private static final Logger log = LoggerFactory.getLogger(HtpRequestConsumer.class);

    public boolean parseHtpRequest (String message){

        // 1. 메시지가 htp 인지 판단
        String[] htpMsg = HtpParser.isHTP(message);
        if (htpMsg == null) {
            log.warn("message is not HTP message. [{}]", message);
            return false;
        }

        // 2. 필수 파라미터 존재하는지 확인
        HtpFormat htpFormat = HtpParser.htpParse(htpMsg);
        if (htpFormat == null) {
            log.warn("Message parsing failed. [{}]", message);
            return false;
        }

        // 3. 요청 종류에 따라 처리
        boolean result = false;
        switch (htpFormat.getType()){
            case HtpType.CONNECT:
                result = inConnect(htpFormat);
                break;
            case HtpType.DISCONNECT:
                result = inDisconnect(htpFormat);
                break;
            case HtpType.ENTER:
                break;
            case HtpType.EXIT:
                break;
            case HtpType.MESSAGE:
                break;
            default:
                log.warn("unknown REQUEST MESSAGE : {}", htpFormat.getType());
                break;
        }
        if (!result) {
            log.warn("REQUEST MESSAGE Processing Fail : {}", message);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @fn private boolean inConnect
     * @brief CONNECT 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean inConnect(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);

        if (userId != null && htpFormat.getBody().containsKey(HtpKey.USER_NAME)){
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (userInfoManager.createUserInfo(htpFormat.getBody().get(HtpKey.USER_ID), htpFormat.getFromIp(), htpFormat.getBody().get(HtpKey.USER_NAME), htpFormat.getFromPort()) == null){
                outDeny(userId, htpFormat);
                return false;
            }

            AppInstance.getInstance().getServerGUI().getUserTextArea().setText(userInfoManager.printUserList());

            outAccept(userId, htpFormat);
            return true;
        } else {
            log.warn("Do not exist userId, userName in REQUEST MESSAGE : {}", htpFormat.getType());
            outDeny(userId, htpFormat);
            return false;
        }
    }

    /**
     * @fn private boolean inDisconnect
     * @brief DISCONNECT 메시지 수신시 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean inDisconnect(HtpFormat htpFormat){
        String userId = htpFormat.getBody().get(HtpKey.USER_ID);

        if (userId != null && htpFormat.getBody().containsKey(HtpKey.USER_NAME)){
            UserInfoManager userInfoManager = UserInfoManager.getInstance();
            if (userInfoManager.deleteUserInfo(htpFormat.getBody().get(HtpKey.USER_ID)) ) {
                outDeny(userId, htpFormat);
                return false;
            }

            AppInstance.getInstance().getServerGUI().getUserTextArea().setText(userInfoManager.printUserList());

            outAccept(userId, htpFormat);
            return true;
        } else {
            log.warn("Do not exist userId, userName in REQUEST MESSAGE : {}", htpFormat.getType());
            outDeny(userId, htpFormat);
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
        String message = HtpCreateMessage.createHtpAccept(htpFormat);

        NettyManager.getInstance().getNettyClient(userId).send(message);
        return true;
    }

    /**
     * @fn private boolean outDeny
     * @brief 요청 메시지에 대한 거부 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean outDeny(String userId, HtpFormat htpFormat){
        String message = HtpCreateMessage.createHtpDeny(htpFormat);

        NettyManager.getInstance().getNettyClient(userId).send(message);
        return true;
    }
}
