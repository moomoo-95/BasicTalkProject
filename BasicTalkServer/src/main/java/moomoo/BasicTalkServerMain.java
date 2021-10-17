package moomoo;

import moomoo.service.ServiceManager;

/**
 * @class public class BasicTalkServerMain
 * @brief BasicTalk Server 의 메인 클래스
 * @author hyeonseong Lim
 */
public class BasicTalkServerMain {

    public static void main(String[] args) {

        ServiceManager.getInstance().loop();
    }
}
