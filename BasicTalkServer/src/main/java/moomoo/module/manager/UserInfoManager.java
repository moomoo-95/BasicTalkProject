package moomoo.module.manager;

import moomoo.module.info.UserInfo;
import moomoo.netty.NettyManager;
import moomoo.netty.module.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @class public class UserInfoManager
 * @brief UserInfo를 관리하는 클래스
 * @author hyeonseong Lim
 */
public class UserInfoManager {

    private static final Logger log = LoggerFactory.getLogger(UserInfoManager.class);

    private static UserInfoManager userInfoManager = null;

    private ConcurrentHashMap<String, UserInfo> userInfoMap = new ConcurrentHashMap<>();

    public UserInfoManager() {

    }

    public static UserInfoManager getInstance() {
        if (userInfoManager == null){
            userInfoManager = new UserInfoManager();
        }
        return userInfoManager;
    }

    public UserInfo createUserInfo (String userId, String userIp, String userName, int userPort){
        // 1. 필수 파라미터 null 확인
        if (userId == null || userIp == null || userName == null || userPort == 0){
            log.warn("not enough parameter userId : {}, userIp : {}, userName = {}, userPort : {}", userId, userIp, userName, userPort);
            return null;
        }
        UserInfo userInfo = new UserInfo(userId, userIp, userName, userPort);

        // 2. 이미 존재하는지 확인
        if (userInfoMap.containsKey(userId)){
            log.warn("already exist userInfo ({})", userId);
            return null;
        }

        // 3. nettyClient 생성
        NettyClient nettyClient = NettyManager.getInstance().createUdpClient(userId, userIp, userPort);
        if (nettyClient == null) {
            log.debug("udpClient ({} {}:{}) cannot be created.", userId, userIp, userPort);
        }

        userInfoMap.put(userId, userInfo);
        log.debug("userInfo ({}) is created", userId);
        return userInfo;
    }

    public boolean deleteUserInfo (String userId){
        // 1. 존재하는지 확인
        if (userInfoMap.containsKey(userId)){
            userInfoMap.remove(userId);
            log.warn("remove userInfo ({})", userId);
            return true;
        } else {
            log.warn("not exist userInfo ({})", userId);
            return false;
        }


    }

    public int getMapSize() {return userInfoMap.size();}

    public String printUserList(){
        StringBuilder result = new StringBuilder();
        userInfoMap.forEach((key, value) -> {
            result.append("ID : " + value.getUserId() +" / NAME : "+ value.getUserName() + "\n");
        });

        return result.toString();
    }
}
