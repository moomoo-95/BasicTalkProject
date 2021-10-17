package moomoo.core.htp.base;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @class public class HTP
 * @brief Hyeonseong Text Protocol Format
 * @author hyeonseong Lim
 *
 * format
 *
 * REQUEST TYPE
 * CONNECT : 유저 정보 등록, DISCONNECT : 등록된 정보 해제, ENTER : conference 입장, EXIT : conference 퇴장, MESSAGE : 단순 전송
 *
 * RESPONSE TYPE
 * ACCEPT : 승인 / DENY : 거절
 *
 * HEADER Format
 * 프로토콜명/버전 REQUEST or RESPONSE
 * FROM: 송신측 URI
 * TO : 수신측 URI
 * LENGTH : BODY 길이
 *
 * BODY parameter
 * userId=user식별자 (필수)
 * userName=user이름 (CONNECT 필수)
 * conferenceId=conferenceId 식별자 (ENTER, EXIT 필수. MESSAGE 옵션 - 없으면 서버의 notice)
 * text=전송할 메시지 (MESSAGE 필수)
 */
public class HtpFormat {
    // HEADER
    private String type;
    private String fromIp;
    private int fromPort;
    private String toIp;
    private int toPort;
    private int length;

    private ConcurrentHashMap<String, String> body = new ConcurrentHashMap<>();

    public HtpFormat() {
        // nothing
    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getFromIp() {return fromIp;}
    public void setFromIp(String fromIp) {this.fromIp = fromIp;}

    public int getFromPort() {return fromPort;}
    public void setFromPort(int fromPort) {this.fromPort = fromPort;}

    public String getToIp() {return toIp;}
    public void setToIp(String toIp) {this.toIp = toIp;}

    public int getToPort() {return toPort;}
    public void setToPort(int toPort) {this.toPort = toPort;}

    public int getLength() {return length;}

    public void setLength(int length) {this.length = length;}

    public ConcurrentHashMap<String, String> getBody() {return body;}

    public void addBody(String key, String value) {this.body.put(key, value);}

    @Override
    public String toString() {
        return "HTPFormat{" +
                "type='" + type + '\'' +
                ", fromIp='" + fromIp + '\'' +
                ", fromPort=" + fromPort +
                ", toIp='" + toIp + '\'' +
                ", toPort=" + toPort +
                ", length=" + length +
                ", body=" + body.toString() +
                '}';
    }
}