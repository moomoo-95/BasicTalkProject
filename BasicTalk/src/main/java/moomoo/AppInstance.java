package moomoo;

/**
 * @class public class AppInstance
 * @brief BasicTalkServer 전역에서 사용하는 데이터
 * @author hyeonseong Lim
 */
public class AppInstance {
    private static final int THREAD_SIZE = 5;
    private static final int SERVER_PORT = 5200;
    // todo 일단 1대1먼저 이므로 고정
    private static final String CLIENT_IP = "192.168.2.163";
    private static final int CLIENT_PORT = 5100;

    public AppInstance() {
        // nothing
    }

    public int getThreadSize(){ return THREAD_SIZE; }

    public int getServerPort(){ return SERVER_PORT; }

    public String getClientIp() { return CLIENT_IP; }
    public int getClientPort(){ return CLIENT_PORT; }

}
