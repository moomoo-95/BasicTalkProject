package moomoo.core.htp.util;

import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.base.HtpType;

/**
 * @class public class HtpCreateMessage
 * @brief HTP 프로토콜 메시지를 생성하는 클래스
 * @author hyeonseong Lim
 */
public class HtpCreateMessage {

    private HtpCreateMessage() {
        // nothing
    }

//    /**
//     * @fn public static String createHtpConnect
//     * @brief 클라이언트 : 서버에게 사용자 등록을 요청하는 Connect 요청 메시지를 만드는 클래스
//     * 서버 : 등록된 사용자들에게 주기적으로 송수신 가능한 상태인지 확인하는 Connect 요청 메시지를 만드는 클래스
//     * @param htpFormat
//     * @return
//     */
//    public static String createHtpConnect(HtpFormat htpFormat) {
//
//    }

    /**
     * @fn public String createHtpAccept
     * @brief 요청에 대한 허락을 의미하는 Accept 응답 메시지를 만드는 클래스
     * @param htpFormat 응답할 대상이 되는 요청 메시지
     * @return HTP 형태의 Accept 메시지
     */
    public static String createHtpAccept(HtpFormat htpFormat){
        StringBuilder result = new StringBuilder();

        result.append(HtpKey.PROTOCOL + " " + HtpType.ACCEPT + "\n");
        result.append(HtpKey.FROM + " " + htpFormat.getToIp() + ":" + htpFormat.getToPort() + "\n");
        result.append(HtpKey.TO + " " + htpFormat.getFromIp() + ":" + htpFormat.getFromPort() + "\n");
        result.append(HtpKey.TRANSACTION + " " + htpFormat.getType() + " " + htpFormat.getTransaction() + "\n");
        result.append(HtpKey.LENGTH + " " + htpFormat.getBodyString().length() + "\n\n");
        result.append(htpFormat.getBodyString());

        return result.toString();
    }

    /**
     * @fn public String createHtpDeny
     * @brief 요청에 대한 거절을 의미하는 Deny 응답 메시지를 만드는 클래스
     * @param htpFormat 응답할 대상이 되는 요청 메시지
     * @return HTP 형태의 Deny 메시지
     */
    public static String createHtpDeny(HtpFormat htpFormat){
        StringBuilder result = new StringBuilder();

        result.append(HtpKey.PROTOCOL + " " + HtpType.DENY + "\n");
        result.append(HtpKey.FROM + " " + htpFormat.getToIp() + ":" + htpFormat.getToPort() + "\n");
        result.append(HtpKey.TO + " " + htpFormat.getFromIp() + ":" + htpFormat.getFromPort() + "\n");
        result.append(HtpKey.TRANSACTION + " " + htpFormat.getType() + " " + htpFormat.getTransaction() + "\n");
        result.append(HtpKey.LENGTH + " " + htpFormat.getBodyString().length() + "\n\n");
        result.append(htpFormat.getBodyString());

        return result.toString();
    }

}
