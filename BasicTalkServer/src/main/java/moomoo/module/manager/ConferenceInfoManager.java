package moomoo.module.manager;

import moomoo.module.info.ConferenceInfo;
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
public class ConferenceInfoManager {

    private static final Logger log = LoggerFactory.getLogger(ConferenceInfoManager.class);

    private static ConferenceInfoManager conferenceInfoManager = null;

    private ConcurrentHashMap<String, ConferenceInfo> conferenceInfoMap = new ConcurrentHashMap<>();

    public ConferenceInfoManager() {

    }

    public static ConferenceInfoManager getInstance() {
        if (conferenceInfoManager == null){
            conferenceInfoManager = new ConferenceInfoManager();
        }
        return conferenceInfoManager;
    }

    public ConferenceInfo createConferenceInfo (String conferenceId, String userId){
        // 1. 필수 파라미터 null 확인
        if (conferenceId == null || userId == null){
            log.warn("not enough parameter conferenceId : {}, userId : {}", conferenceId, userId);
            return null;
        }
        ConferenceInfo conferenceInfo = new ConferenceInfo(conferenceId, userId);

        // 2. 이미 존재하는지 확인
        if (conferenceInfoMap.containsKey(conferenceId)){
            log.warn("already exist conferenceInfo ({})", conferenceId);
            return null;
        }

        conferenceInfoMap.put(conferenceId, conferenceInfo);
        log.debug("conferenceInfo ({}) is created", conferenceId);
        return conferenceInfo;
    }

    public boolean deleteConferenceInfo (String conferenceId){
        // 1. 존재하는지 확인
        if (conferenceInfoMap.containsKey(conferenceId)){
            conferenceInfoMap.remove(conferenceId);
            log.warn("remove conferenceInfo ({})", conferenceId);
            return true;
        } else {
            log.warn("not exist conferenceInfo ({})", conferenceId);
            return false;
        }


    }

    public ConferenceInfo getConferenceInfo(String conferenceId) {
        ConferenceInfo conferenceInfo = null;
        synchronized (conferenceInfoMap) {
            conferenceInfo = conferenceInfoMap.get(conferenceId);
        }
        return conferenceInfo;
    }

    public int getMapSize() {return conferenceInfoMap.size();}

    public String printConferenceList(){
        StringBuilder result = new StringBuilder();
        conferenceInfoMap.forEach((key, value) -> {
            result.append("Conference ID : " + value.getConferenceId() + "\n");
        });

        return result.toString();
    }

    public ConcurrentHashMap<String, ConferenceInfo> getConferenceInfoMap() { return conferenceInfoMap; }
}
