package moomoo.service;


import moomoo.AppInstance;
import moomoo.gui.ClientGUI;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


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


    private void startService() {
        AppInstance instance = AppInstance.getInstance();

        try {
            instance.setIp( InetAddress.getLocalHost().getHostAddress() );
        } catch (Exception e){
            log.error("AppInstance.ipSetting.Exception ", e);
        }

        boolean isUsed = true;
        while (isUsed && instance.getPort() < 5300) {
            try {
                instance.setUsedPort(new DatagramSocket(instance.getPort()));
                instance.getUsedPort().close();

                NettyManager.getInstance().startUdp();

                isUsed = false;
            } catch (Exception e) {
                instance.setPort(instance.getPort()+1);
            }
        }
        instance.setUserId( "HTP_"+(int)(Math.random()*10000000) );
        String title = "BasicTalk (URL :" + instance.getIp() + ":" + instance.getPort() + ")";
        instance.setClientGUI( new ClientGUI(title) );
    }

    private void stopService(){
        AppInstance.getInstance().getUsedPort().close();
        NettyManager.getInstance().stopUdp();
    }
}