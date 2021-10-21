package moomoo.module.info;

import io.netty.util.internal.ConcurrentSet;

/**
 * @class public class ConferenceInfo
 * @brief Conference 정보를 담은 클래스
 * @author hyeonseong Lim
 */
public class ConferenceInfo {

    private final String conferenceId;
    private final ConcurrentSet<String> userSet = new ConcurrentSet<>();

    public ConferenceInfo(String conferenceId, String userId) {
        this.conferenceId = conferenceId;
        addUserSet(userId);
    }

    public String getConferenceId() { return conferenceId; }

    public void addUserSet(String userId) {
        userSet.add(userId);
    }

    public void removeUserSet(String userId) {
        userSet.remove(userId);
    }

    public boolean isEmpty() { return userSet.isEmpty(); }

    public void clear() { userSet.clear(); }
}
