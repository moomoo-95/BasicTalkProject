package moomoo.netty;

import moomoo.AppInstance;
import moomoo.netty.module.NettyClient;
import moomoo.netty.module.NettyServer;

/**
 * @class public class NettyManager
 * @brief udp 송수신을 담당하는 nettyclient/server를 관리하는 매니저 클래스
 * @author hyeonseong Lim
 */
public class NettyManager {

    private static final AppInstance appInstance = AppInstance.getInstance();
    private static NettyManager nettyManager = null;

    private NettyServer nettyServer;
    private NettyClient nettyClient;

    public NettyManager() {
        // nothing
    }

    public static NettyManager getInstance() {
        if (nettyManager == null){
            nettyManager = new NettyManager();
        }
        return nettyManager;
    }

    public void startUdp() {
        startUdpServer();
        startUdpClient();
    }

    public void stopUdp() {
        stopUdpServer();
        stopUdpClient();
    }

    public void startUdpServer() {
        nettyServer = new NettyServer(AppInstance.THREAD_SIZE, AppInstance.getInstance().getPort());
        nettyServer.run();
    }

    public void startUdpClient() {
        nettyClient = new NettyClient(AppInstance.THREAD_SIZE, AppInstance.REMOTE_IP,  AppInstance.REMOTE_PORT);
        nettyClient.run();
    }

    public void stopUdpServer() {
        nettyServer.close();
    }

    public void stopUdpClient() {
        nettyClient.close();
    }

    public NettyServer getNettyServer() { return nettyServer; }

    public NettyClient getNettyClient() { return nettyClient; }
}
