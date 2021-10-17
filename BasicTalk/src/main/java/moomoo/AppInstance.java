package moomoo;

/**
 * @class public class AppInstance
 * @brief BasicTalkServer 전역에서 사용하는 데이터
 * @author hyeonseong Lim
 */
public class AppInstance {
    public static final int THREAD_SIZE = 5;
    public static final int PORT = 5200;
    // todo 일단 1대1먼저 이므로 고정
    public static final String REMOTE_IP = "127.0.0.1";
    public static final int REMOTE_PORT = 5100;
}
