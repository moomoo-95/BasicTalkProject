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

    /**
     * @fn public String createHtpAccept
     * @brief Accept 메시지를 만드는 클래스
     * @param htpFormat
     * @return
     */
    public static String createHtpAccept(HtpFormat htpFormat){


        StringBuilder result = new StringBuilder();

        StringBuilder body = new StringBuilder();

        htpFormat.getBody().forEach((key, value) -> body.append(key + "=" + value + "\n"));

        result.append(HtpKey.PROTOCOL + " " + HtpType.ACCEPT + "\n");
        result.append(HtpKey.FROM + " " + htpFormat.getToIp() + ":" + htpFormat.getToPort() + "\n");
        result.append(HtpKey.TO + " " + htpFormat.getFromIp() + ":" + htpFormat.getFromPort() + "\n");
        result.append(HtpKey.LENGTH + " " + body.toString().length() + "\n\n");
        result.append(body);

        return result.toString();
    }

    /**
     * @fn public String createHtpDeny
     * @brief Deny 메시지를 만드는 클래스
     * @param htpFormat
     * @return
     */
    public static String createHtpDeny(HtpFormat htpFormat){


        StringBuilder result = new StringBuilder();

        StringBuilder body = new StringBuilder();

        htpFormat.getBody().forEach((key, value) -> body.append(key + "=" + value + "\n"));

        result.append(HtpKey.PROTOCOL + " " + HtpType.DENY + "\n");
        result.append(HtpKey.FROM + " " + htpFormat.getToIp() + ":" + htpFormat.getToPort() + "\n");
        result.append(HtpKey.TO + " " + htpFormat.getFromIp() + ":" + htpFormat.getFromPort() + "\n");
        result.append(HtpKey.LENGTH + " " + body.toString().length() + "\n\n");
        result.append(body);

        return result.toString();
    }

}
