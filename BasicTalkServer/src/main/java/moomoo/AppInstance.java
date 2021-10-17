package moomoo;

import moomoo.gui.ServerGUI;

/**
 * @class public class AppInstance
 * @brief BasicTalkServer 전역에서 사용하는 데이터
 * @author hyeonseong Lim
 */
public class AppInstance {

    public static final int PORT = 5100;
    public static final int SERVER_THREAD_SIZE = 256;
    public static final int CLIENT_THREAD_SIZE = 3;

    private static AppInstance appInstance = null;
    private ServerGUI serverGUI = null;

    public AppInstance() {
        String title = "BasicTalk Server (PORT : " + PORT + ")";
        serverGUI = new ServerGUI(title);
    }

    public static AppInstance getInstance() {
        if (appInstance == null) {
            appInstance = new AppInstance();
        }
        return appInstance;
    }

    public ServerGUI getServerGUI() {return serverGUI;}
}
