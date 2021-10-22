package moomoo;

import moomoo.gui.ServerGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class public class AppInstance
 * @brief BasicTalkServer 전역에서 사용하는 데이터
 * @author hyeonseong Lim
 */
public class AppInstance {

    private static final Logger log = LoggerFactory.getLogger(AppInstance.class);

    public static final int PORT = 5100;
    public static final int SERVER_THREAD_SIZE = 256;
    public static final int CLIENT_THREAD_SIZE = 3;

    private static AppInstance appInstance = null;
    private ServerGUI serverGUI = null;

    private String ip = "192.168.2.163";
//    private String ip = "127.0.0.1";

    public AppInstance() {
        // nothing
    }

    public static AppInstance getInstance() {
        if (appInstance == null) {
            appInstance = new AppInstance();
        }
        return appInstance;
    }

    public ServerGUI getServerGUI() {return serverGUI;}
    public void setServerGUI(ServerGUI serverGUI) {this.serverGUI = serverGUI;}

    public String getIp() { return ip;}
    public void setIp(String ip) {this.ip = ip;}
}
