package moomoo.core.htp.base;

/**
 * @class public class HtpType
 * @brief HTP 메시지 타입
 *
 *
 * REQUEST TYPE
 * CONNECT : 유저 정보 등록, DISCONNECT : 등록된 정보 해제, ENTER : conference 입장, EXIT : conference 퇴장, MESSAGE : 단순 전송
 *
 * RESPONSE TYPE
 * ACCEPT : 승인 / DENY : 거절
 */
public class HtpType {

    private HtpType() {
        // nothing
    }

    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String ENTER = "ENTER";
    public static final String EXIT = "EXIT";
    public static final String MESSAGE = "MESSAGE";

    public static final String ACCEPT = "ACCEPT";
    public static final String DENY = "DENY";
}
