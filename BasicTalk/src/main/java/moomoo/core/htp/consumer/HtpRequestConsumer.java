package moomoo.core.htp.consumer;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpType;
import moomoo.core.htp.process.HtpIncomingMessage;
import moomoo.core.htp.util.HtpParser;
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
        boolean result = false;

        // 1. 메시지가 htp 인지 판단
        String[] htpMsg = HtpParser.isHTP(message);
        if (htpMsg == null) {
            log.warn("message is not HTP message. [{}]", message);
            return result;
        }

        // 2. 필수 파라미터 존재하는지 확인
        HtpFormat htpFormat = HtpParser.htpParse(htpMsg);
        if (htpFormat == null) {
            log.warn("Message parsing failed. [{}]", message);
            return result;
        }

        if (htpFormat.getTransaction() < AppInstance.getInstance().getTransactionSeq().get()) {
            log.warn("Message is too old. [{}]", message);
            return result;
        }

        // 3. 요청 종류에 따라 처리
        switch (htpFormat.getType()){
            case HtpType.MESSAGE:
                result = htpIncomingMessage.inMessage(htpFormat);
                break;
            case HtpType.CONNECT:
            case HtpType.DISCONNECT:
            case HtpType.ENTER:
            case HtpType.EXIT:
                log.debug("INPUT REQUEST MESSAGE : {}", htpFormat.getType());
                break;
            default:
                log.warn("unknown REQUEST MESSAGE : {}", htpFormat.getType());
                break;
        }
        if (!result) {
            log.warn("REQUEST MESSAGE Processing Fail : {}", message);
        }
        return result;
    }

}
