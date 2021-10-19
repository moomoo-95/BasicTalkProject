package moomoo.core.htp.base;

/**
 * @class public class HtpBodyKey
 * @brief HTP 키 목록
 *
 * HEADER parameter
 * 프로토콜명/버전 REQUEST or RESPONSE
 * FROM: 송신측 URL
 * TO : 수신측 URL
 * TRANSACTION : 트랜잭션 구분
 * LENGTH : BODY 길이
 *
 * BODY parameter
 * userId=user식별자 (필수)
 * userName=user이름 (CONNECT 필수)
 * conferenceId=conferenceId 식별자 (ENTER, EXIT 필수. MESSAGE 옵션 - 없으면 서버의 notice)
 * text=전송할 메시지 (MESSAGE 필수)
 */
public class HtpKey {

    private HtpKey() {
        // nothing
    }

    public static final String PROTOCOL = "HTP/1.0";
    public static final String FROM = "FROM:";
    public static final String TO = "TO:";
    public static final String TRANSACTION = "TRANSACTION:";
    public static final String LENGTH = "LENGTH:";

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String CONFERENCE_ID = "conferenceId";
    public static final String TEXT = "text";
}
