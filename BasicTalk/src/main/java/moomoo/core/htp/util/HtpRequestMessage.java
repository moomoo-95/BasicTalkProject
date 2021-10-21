package moomoo.core.htp.util;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.base.HtpType;

/**
 * @class public class HtpRequestMessage
 * @brief HTP 프로토콜 요청 메시지를 생성하는 클래스
 * @author hyeonseong Lim
 */
public class HtpRequestMessage {

    private static final AppInstance instance = AppInstance.getInstance();

    /**
     * @fn public static String createHtpConnect
     * @brief 클라이언트 : 서버에게 사용자 등록을 요청하는 Connect 요청 메시지를 만드는 클래스
     * @return
     */
    public static String createHtpConnect() {
        HtpFormat htpFormat = createHeaderFormat(HtpType.CONNECT);

        htpFormat.addBody(HtpKey.USER_ID, instance.getUserId());
        htpFormat.addBody(HtpKey.USER_NAME, instance.getUserName());

        htpFormat.setLength(htpFormat.getBodyString().length());

        return htpFormat.getHeaderString() + "\n" + htpFormat.getBodyString();
    }

    /**
     * @fn public static String createHtpDisconnect
     * @brief 클라이언트 : 사용자 등록 해제를 요청하는 Disconnect 요청 메시지를 만드는 클래스
     * @return
     */
    public static String createHtpDisconnect() {
        HtpFormat htpFormat = createHeaderFormat(HtpType.DISCONNECT);

        htpFormat.addBody(HtpKey.USER_ID, instance.getUserId());

        htpFormat.setLength(htpFormat.getBodyString().length());

        return htpFormat.getHeaderString() + "\n" + htpFormat.getBodyString();
    }

    /**
     * @fn public static String createHtpEnter
     * @brief 클라이언트 : 사용자가 회의방 생성 또는 입장을 요청하는 Enter 요청 메시지를 만드는 클래스
     * @return
     */
    public static String createHtpEnter(String conferenceId) {
        HtpFormat htpFormat = createHeaderFormat(HtpType.ENTER);

        htpFormat.addBody(HtpKey.USER_ID, instance.getUserId());
        htpFormat.addBody(HtpKey.CONFERENCE_ID, conferenceId);

        htpFormat.setLength(htpFormat.getBodyString().length());

        return htpFormat.getHeaderString() + "\n" + htpFormat.getBodyString();
    }

    /**
     * @fn public static String createHtpExit
     * @brief 클라이언트 : 사용자가 회의방 퇴장 또는 제거를 요청하는 Exit 요청 메시지를 만드는 클래스
     * @return
     */
    public static String createHtpExit() {
        HtpFormat htpFormat = createHeaderFormat(HtpType.EXIT);

        htpFormat.addBody(HtpKey.USER_ID, instance.getUserId());
        htpFormat.addBody(HtpKey.CONFERENCE_ID, instance.getConferenceId());

        htpFormat.setLength(htpFormat.getBodyString().length());

        return htpFormat.getHeaderString() + "\n" + htpFormat.getBodyString();
    }

    /**
     * @fn public static String createHtpMessage
     * @brief 클라이언트 : 참여하고 있는 회의방 내 모든 사용자에게 text를 전송하는 Message 요청 메시지를 만드는 클래스
     * 서버 : 모든 등록된 사용자들에게 text를 공지하는 Message 요청 메시지를 만드는 클래스
     * @return
     */
    public static String createHtpMessage(String text) {
        HtpFormat htpFormat = createHeaderFormat(HtpType.MESSAGE);

        htpFormat.addBody(HtpKey.USER_ID, instance.getUserId());
        htpFormat.addBody(HtpKey.CONFERENCE_ID, instance.getConferenceId());
        htpFormat.addBody(HtpKey.TEXT, text);

        htpFormat.setLength(htpFormat.getBodyString().length());

        return htpFormat.getHeaderString() + "\n" + htpFormat.getBodyString();
    }


    /**
     * @fn private static HtpFormat createHeaderFormat
     * @brief HtpFormat 내부에 body 길이를 제외한 나머지 헤더를 세팅하는 메서드
     * @return 헤더가 세팅된 HtpFormat (길이 제외)
     */
    private static HtpFormat createHeaderFormat(String Type) {
        HtpFormat htpFormat = new HtpFormat();
        htpFormat.setType(Type);
        htpFormat.setFromIp(instance.IP);
        htpFormat.setFromPort(instance.PORT);
        htpFormat.setToIp(AppInstance.REMOTE_IP);
        htpFormat.setToPort(AppInstance.REMOTE_PORT);
        htpFormat.setTransaction(instance.getTransactionSeq().incrementAndGet());

         return htpFormat;
    }
}
