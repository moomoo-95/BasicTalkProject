package moomoo.module.info;

/**
 * @class public class UserInfo
 * @brief User 정보를 담은 클래스
 * @author hyeonseong Lim
 */
public class UserInfo {

    private final String userId;
    private final String userIp;
    private final String userName;
    private final int userPort;

    private int transactionSeq;
    private String conferenceId;

    public UserInfo (String userId, String userIp, String userName, int userPort, int transactionSeq){
        this.userId = userId;
        this.userIp = userIp;
        this.userName = userName;
        this.userPort = userPort;
        this.transactionSeq = transactionSeq;
        this.conferenceId = "";
    }

    public String getUserId() {return userId;}

    public String getUserIp() {return userIp;}

    public String getUserName() {return userName;}

    public int getUserPort() {return userPort;}

    public int getTransactionSeq() {return transactionSeq;}

    public void setTransactionSeq(int transactionSeq) {this.transactionSeq = transactionSeq;}

    public String getConferenceId() {return conferenceId;}
    public void setConferenceId(String conferenceId) {this.conferenceId = conferenceId;}
}
