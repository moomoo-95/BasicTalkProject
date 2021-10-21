package moomoo;

import moomoo.gui.ClientGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @class public class AppInstance
 * @brief BasicTalkServer 전역에서 사용하는 데이터
 * @author hyeonseong Lim
 */
public class AppInstance {

    private static final Logger log = LoggerFactory.getLogger(AppInstance.class);

    public static final int THREAD_SIZE = 5;

//    public static final String REMOTE_IP = "192.168.2.163";
    public static final String REMOTE_IP = "192.168.219.101";
    public static final int REMOTE_PORT = 5100;

    private static AppInstance instance = null;
    private static DatagramSocket usedPort;
    private ClientGUI clientGUI = null;

    private String ip = "127.0.0.1";
    private int port = 5200;
    private String userId;
    private String userName;
    private String conferenceId;
    private AtomicInteger transactionSeq = new AtomicInteger();


    public AppInstance() {
        // nothing
    }

    public static AppInstance getInstance() {
        if (instance == null) {
            instance = new AppInstance();
        }
        return instance;
    }

    public ClientGUI getClientGUI() { return clientGUI; }
    public void setClientGUI(ClientGUI clientGUI) {this.clientGUI = clientGUI;}

    public DatagramSocket getUsedPort() {return usedPort;}
    public void setUsedPort(DatagramSocket usedPort) {AppInstance.usedPort = usedPort;}

    public String getIp() {return ip;}
    public void setIp(String ip) {this.ip = ip;}

    public int getPort() {return port;}
    public void setPort(int port) {this.port = port;}

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getConferenceId() {return conferenceId;}
    public void setConferenceId(String conferenceId) {this.conferenceId = conferenceId;}

    public AtomicInteger getTransactionSeq() {return transactionSeq;}
}
