package moomoo.netty;

import moomoo.AppInstance;
import moomoo.netty.module.NettyClient;
import moomoo.netty.module.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @class public class NettyManager
 * @brief udp 송수신을 담당하는 nettyclient/server를 관리하는 매니저 클래스
 * @author hyeonseong Lim
 */
public class NettyManager {

    private static final Logger log = LoggerFactory.getLogger(NettyManager.class);

    private static NettyManager nettyManager = null;

    private NettyServer nettyServer;
    private ConcurrentHashMap<String, NettyClient> clientMap = new ConcurrentHashMap<>();

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
    }

    public void stopUdp() {
        stopUdpServer();
        stopUdpClient();
    }

    public void startUdpServer() {
        nettyServer = new NettyServer(AppInstance.SERVER_THREAD_SIZE, AppInstance.PORT);
        nettyServer.run();
    }

    public NettyClient createUdpClient(String userId, String userIp, int userPort) {
        try {
            // 1. 존재하는지 확인
            if(clientMap.containsKey(userId)){
                log.warn("already exist udpClient ({} {}:{})", userId, userIp, userPort);
                return clientMap.get(userId);
            }
            NettyClient nettyClient = new NettyClient(AppInstance.CLIENT_THREAD_SIZE, userIp, userPort);
            nettyClient.run();

            clientMap.put(userId, nettyClient);
            log.debug("udpClient ({} {}:{}) is created", userId, userIp, userPort);
            return nettyClient;
        } catch (Exception e){
            log.error("NettyManager.createUdpClient.Exception ", e);
            return null;
        }
    }

    public boolean deleteUdpClient(String userId){
        if (clientMap.containsKey(userId)){
            NettyClient nettyClient = clientMap.remove(userId);
            nettyClient.close();
            log.warn("remove userInfo ({})", userId);
            return true;
        } else {
            log.warn("not exist userInfo ({})", userId);
            return false;
        }
    }

    public void stopUdpServer() {
        nettyServer.close();
    }

    public void stopUdpClient() {
        clientMap.forEach((userId, client) -> client.close());
        clientMap.clear();
    }

    public NettyServer getNettyServer() { return nettyServer; }

    public NettyClient getNettyClient(String userId) {
        // 1. 존재하는지 확인
        if(!clientMap.containsKey(userId)){
            log.warn("Do not exist udpClient ({})", userId);
            return null;
        }
        return clientMap.get(userId);
    }

}
