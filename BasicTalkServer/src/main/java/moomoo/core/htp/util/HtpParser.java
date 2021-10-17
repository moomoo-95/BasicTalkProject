package moomoo.core.htp.util;

import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @fn public String[] isHTP
     * @brief 해당 메시지가 HTP 의 규격에 맞는지 확인하는 메서드
     * @param htpMessage 검사할 메시지
     * @return 배열로 변환된 검사한 메시지 또는 NULL
     */
    public static String[] isHTP(String htpMessage){
        String[] message = htpMessage.split("\n");

        if (message[0].split(" ")[0].equals(HtpKey.PROTOCOL)){
            return message;
        }
        log.warn("[{}] is not HTP Message", htpMessage);
        return null;
    }

    /**
     * @fn public HTPFormat HTPParse
     * @brief 검증된 메시지를 분석하는 메서드
     * @param htpMessage 분석할 메시지
     * @return 분석완료한 메시지를 담은 HTP 포맷 또는 NULL
     */
    public static HtpFormat htpParse(String[] htpMessage){
        HtpFormat message = new HtpFormat();

        boolean isHeader = true;
        for (int i = 0; i < htpMessage.length; i++){
            // 공백 이후 부터 body
            if (htpMessage[i].equals("")) {
                isHeader = false;
                continue;
            }
            // header parse
            if (isHeader) {
                String[] parseLine = htpMessage[i].split(" ");
                if(parseLine.length != 2){
                    log.warn("message format is not right. line : [{}]", htpMessage[i]);
                    return null;
                }
                switch (parseLine[0]){
                    case HtpKey.PROTOCOL:
                        message.setType(parseLine[1]);
                        break;
                    case HtpKey.FROM:
                    case HtpKey.TO:
                        String[] uri = parseLine[1].split(":");
                        if(uri.length != 2){
                            log.warn("message format is not right. line : [{}]", htpMessage[i]);
                            return null;
                        }
                        if (parseLine[0].equals(HtpKey.FROM)){
                            message.setFromIp(uri[0]);
                            message.setFromPort(Integer.parseInt(uri[1]));
                        } else {
                            message.setToIp(uri[0]);
                            message.setToPort(Integer.parseInt(uri[1]));
                        }
                        break;
                    case HtpKey.LENGTH:
                        message.setLength(Integer.parseInt(parseLine[1]));
                        break;
                    default:
                        log.warn("unknown header line : [{}]", htpMessage[i]);
                        break;
                }

            }
            // body parse
            else {
                String[] parseLine = htpMessage[i].split("=");
                message.addBody(parseLine[0], parseLine[1]);
            }
        }

        if (message.getBody().containsKey(HtpKey.USER_ID)) {
            log.debug("Message parsing completed. [{}]", message);
            return message;
        }
        log.warn("no userId in Message. [{}]", message);
        return null;
    }
}
