package moomoo.core.htp.util;

import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.base.HtpType;

/**
 * @class public class HtpResponseMessage
 * @brief HTP 프로토콜 응답 메시지를 생성하는 클래스
 * @author hyeonseong Lim
 */
public class HtpResponseMessage {

    private HtpResponseMessage() {
        // nothing
    }

    /**
     * @fn public String createHtpAccept
     * @brief 요청에 대한 허락을 의미하는 Accept 응답 메시지를 만드는 클래스
     * @param htpFormat 응답할 대상이 되는 요청 메시지
     * @return HTP 형태의 Accept 메시지
     */
    public static String createHtpAccept(HtpFormat htpFormat){
        StringBuilder result = createHeaderFormat(HtpType.ACCEPT, htpFormat);
        // 바디는 그대로
        result.append(htpFormat.getBodyString());
        return result.toString();
    }

    /**
     * @fn public String createHtpDeny
     * @brief 요청에 대한 거절을 의미하는 Deny 응답 메시지를 만드는 클래스
     * @param htpFormat 응답할 대상이 되는 요청 메시지
     * @return HTP 형태의 Deny 메시지
     */
    public static String createHtpDeny(HtpFormat htpFormat, String reason){
        // 바디 text 값 변경
        if (htpFormat.getBody().containsKey(HtpKey.TEXT)) {
            htpFormat.getBody().replace(HtpKey.TEXT, reason);
        } else {
            htpFormat.getBody().put(HtpKey.TEXT, reason);
        }

        StringBuilder result = createHeaderFormat(HtpType.DENY, htpFormat);

        result.append(htpFormat.getBodyString());
        return result.toString();
    }

    /**
     * @fn private static HtpFormat createHeaderFormat
     * @brief HtpFormat 내부에 body 길이를 제외한 나머지 헤더를 세팅하는 메서드
     * @return 헤더가 세팅된 HtpFormat (길이 제외)
     */
    private static StringBuilder createHeaderFormat(String type, HtpFormat htpFormat) {
        StringBuilder result = new StringBuilder();

        // 헤더 to 와 from 변경
        result.append(HtpKey.PROTOCOL + " " + type + "\n");
        result.append(HtpKey.FROM + " " + htpFormat.getToIp() + ":" + htpFormat.getToPort() + "\n");
        result.append(HtpKey.TO + " " + htpFormat.getFromIp() + ":" + htpFormat.getFromPort() + "\n");
        result.append(HtpKey.TRANSACTION + " " + htpFormat.getType() + " " + htpFormat.getTransaction() + "\n");
        result.append(HtpKey.LENGTH + " " + htpFormat.getBodyString().length() + "\n\n");

        return result;
    }

}
