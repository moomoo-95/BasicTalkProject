package moomoo.core.htp.util;

import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @class public class HTPParser
 * @brief HTP 프로토콜을 parse 하는 클래스
 * @author hyeonseong Lim
 */
public class HtpParser {

    private static final Logger log = LoggerFactory.getLogger(HtpParser.class);

    private HtpParser() {
        // nothing
    }

    /**
     * @param htpMessage 검사할 메시지
     * @return 배열로 변환된 검사한 메시지 또는 NULL
     * @fn public String[] isHTP
     * @brief 해당 메시지가 HTP 의 규격에 맞는지 확인하는 메서드
     */
    public static String[] isHTP(String htpMessage) {
        String[] message = htpMessage.split("\n");

        if (message[0].split(" ")[0].equals(HtpKey.PROTOCOL)) {
            return message;
        }
        log.warn("[{}] is not HTP Message", htpMessage);
        return null;
    }

    /**
     * @param htpMessage 분석할 메시지
     * @return 분석완료한 메시지를 담은 HTP 포맷 또는 NULL
     * @fn public HTPFormat HTPParse
     * @brief 검증된 메시지를 분석하는 메서드
     */
    public static HtpFormat htpParse(String[] htpMessage) {
        HtpFormat message = new HtpFormat();

        int bodyStart = -1;
        for (int i = 0; i < htpMessage.length; i++) {
            // 공백 이후 부터 body
            if (htpMessage[i].equals("")) {
                bodyStart = i + 1;
                break;
            }
        }

        String[] headerMsg;
        String[] bodyMsg;
        if (bodyStart == -1) {
            headerMsg = htpMessage;

            if (!htpHeaderParser(message, headerMsg)) {
                return null;
            }
        } else {
            headerMsg = Arrays.copyOfRange(htpMessage, 0, bodyStart - 1);
            bodyMsg = Arrays.copyOfRange(htpMessage, bodyStart, htpMessage.length);

            if (!htpHeaderParser(message, headerMsg)) {
                return null;
            }
            if (!htpBodyParser(message, bodyMsg)) {
                return null;
            }
        }

        return message;
    }

    private static boolean htpHeaderParser(HtpFormat message, String[] headerMsg){
        // header parse
        for (int i = 0; i < headerMsg.length; i++) {
            String[] parseLine = headerMsg[i].split(" ");
            if(parseLine.length < 2){
                log.warn("message format is not right. line : [{}]", headerMsg[i]);
                return false;
            }

            switch (parseLine[0]){
                case HtpKey.PROTOCOL:
                    message.setType(parseLine[1]);
                    break;
                case HtpKey.FROM:
                case HtpKey.TO:
                    String[] uri = parseLine[1].split(":");
                    if(uri.length != 2){
                        log.warn("message format is not right. line : [{}]", headerMsg[i]);
                        return false;
                    }
                    if (parseLine[0].equals(HtpKey.FROM)){
                        message.setFromIp(uri[0]);
                        message.setFromPort(Integer.parseInt(uri[1]));
                    } else {
                        message.setToIp(uri[0]);
                        message.setToPort(Integer.parseInt(uri[1]));
                    }
                    break;
                case HtpKey.TRANSACTION:
                    if(parseLine.length < 3){
                        log.warn("message format is not right. line : [{}]", headerMsg[i]);
                        return false;
                    }
                    message.setTransaction(Integer.parseInt(parseLine[2]));
                    break;
                case HtpKey.LENGTH:
                    message.setLength(Integer.parseInt(parseLine[1]));
                    break;
                default:
                    log.warn("unknown header line : [{}]", headerMsg[i]);
                    break;
            }
        }
        return true;
    }

    private static boolean htpBodyParser(HtpFormat message, String[] bodyMsg) {
        StringBuilder textBody = null;
        for (int i = 0; i < bodyMsg.length ; i++){
            if (bodyMsg[i].contains("=") && HtpKey.NOT_TEXT.contains(bodyMsg[i].split("=")[0])) {
                String[] parseLine = bodyMsg[i].split("=");
                if(message.getBody().containsKey(parseLine[0]) || parseLine.length != 2) {
                    log.warn("duplicated body line : [{}]", bodyMsg[i]);
                    continue;
                }

                if(parseLine.length != 2){
                    log.warn("message format is not right. line : [{}]", bodyMsg[i]);
                    return false;
                }
                message.addBody(parseLine[0], parseLine[1]);
            } else {
                if (textBody == null) {
                    textBody = new StringBuilder();
                }
                textBody.append(bodyMsg[0]);
            }
        }

        if (textBody != null){
            String[] parseLine = textBody.toString().split("=");
            message.addBody(parseLine[0], parseLine[1]);
        }
        return true;
    }
}
