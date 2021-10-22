package moomoo.service;

import moomoo.AppInstance;
import moomoo.gui.ServerGUI;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;


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
            log.error("ServiceManager.loop.Exception", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Process is about to quit (Ctrl+C)");
            this.stopService();
        }));
    }


    private void startService(){
        AppInstance instance = AppInstance.getInstance();

//        try {
//            instance.setIp( InetAddress.getLocalHost().getHostAddress() );
//        } catch (Exception e){
//            log.error("AppInstance.ipSetting.Exception ", e);
//        }

        NettyManager.getInstance().startUdp();

        String title = "BasicTalk Server (URL :" + instance.getIp() + ":" + AppInstance.PORT + ")";
        instance.setServerGUI( new ServerGUI(title) );

    }

    private void stopService(){
        NettyManager.getInstance().stopUdp();
    }
}
