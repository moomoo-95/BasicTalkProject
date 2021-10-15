package moomoo.service;

import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @class public class ServiceManager
 * @brief BasicTalk Server 의 Service 를 관리 하는 클래스
 * @author hyeonseong Lim
 */
public class ServiceManager {

    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private static ServiceManager serviceManager = null;

    public ServiceManager() {
        // nothing
    }

    public static ServiceManager getInstance() {
        if (serviceManager == null) {
            serviceManager = new ServiceManager();
        }
        return serviceManager;
    }

    public void loop() {
        try {
            this.startService();
        } catch (Exception e){
            log.error("ServiceManager.loop", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Process is about to quit (Ctrl+C)");
            this.stopService();
        }));
    }


    private void startService(){
        NettyManager.getInstance().startUdp();
    }

    private void stopService(){
        NettyManager.getInstance().stopUdp();
    }
}
