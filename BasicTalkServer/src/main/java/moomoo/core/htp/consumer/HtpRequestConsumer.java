package moomoo.core.htp.consumer;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.base.HtpType;
import moomoo.core.htp.process.HtpIncomingMessage;
import moomoo.core.htp.util.HtpResponseMessage;
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
    private static final HtpIncomingMessage htpIncomingMessage = new HtpIncomingMessage();

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


//        String userId = htpFormat.getBody().get(HtpKey.USER_ID);
//        if (htpFormat.getTransaction() < UserInfoManager.getInstance().getUserInfo(userId).getTransactionSeq()) {
//            log.warn("Message is too old. [{}]", message);
//            return false;
//        }

        // 3. 요청 종류에 따라 처리
        boolean result = false;
        switch (htpFormat.getType()){
            case HtpType.CONNECT:
                result = htpIncomingMessage.inConnect(htpFormat);
                break;
            case HtpType.DISCONNECT:
                result = htpIncomingMessage.inDisconnect(htpFormat);
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

}
