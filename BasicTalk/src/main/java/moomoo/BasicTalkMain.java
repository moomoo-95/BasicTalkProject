package moomoo;

import moomoo.service.ServiceManager;

/**
 * @class public class BasicTalkMain
 * @brief BasicTalk 의 메인 클래스
 * @author hyeonseong Lim
 */
public class BasicTalkMain {

    public static void main(String[] args) {
//        if (args.length != 2) {
//        } else {
//            String host = args[0];
//            int port = Integer.parseInt(args[1]);
//            new ServiceManager(host, port).start();
//        }
        ServiceManager.getInstance().loop();
    }
}
