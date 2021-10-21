package moomoo;

import moomoo.service.ServiceManager;

/**
 * @class public class BasicTalkMain
 * @brief BasicTalk 의 메인 클래스
 * @author hyeonseong Lim
 */
public class BasicTalkMain {

    public static void main(String[] args) {
        ServiceManager.getInstance().loop();
    }
}
