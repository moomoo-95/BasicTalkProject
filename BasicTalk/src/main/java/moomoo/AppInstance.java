package moomoo;

import moomoo.gui.ClientGUI;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @class public class AppInstance
 * @brief BasicTalkServer 전역에서 사용하는 데이터
 * @author hyeonseong Lim
 */
public class AppInstance {

    public static final String IP = "192.168.2.163";
    public static final int PORT = 5200;
    public static final int THREAD_SIZE = 5;

    public static final String REMOTE_IP = "192.168.2.163";
    public static final int REMOTE_PORT = 5100;

    private static AppInstance instance = null;
    private ClientGUI clientGUI = null;

    private String userId;
    private String userName;
    private String conferenceId;
    private AtomicInteger transactionSeq = new AtomicInteger();

    public AppInstance() {
        String title = "BasicTalk (PORT : " + PORT + ")";
        clientGUI = new ClientGUI(title);
    }

    public static AppInstance getInstance() {
        if (instance == null) {
            instance = new AppInstance();
        }
        return instance;
    }

    public ClientGUI getClientGUI() { return clientGUI; }

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getConferenceId() {return conferenceId;}
    public void setConferenceId(String conferenceId) {this.conferenceId = conferenceId;}

    public AtomicInteger getTransactionSeq() {return transactionSeq;}
}
