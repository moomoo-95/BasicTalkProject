package moomoo.core.htp.process;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpFormat;
import moomoo.core.htp.base.HtpKey;
import moomoo.core.htp.base.HtpType;
import moomoo.core.htp.util.HtpResponseMessage;
import moomoo.netty.NettyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtpIncomingMessage {

    private static final Logger log = LoggerFactory.getLogger(HtpIncomingMessage.class);
    private static final AppInstance instance = AppInstance.getInstance();

    /**
     * @fn private boolean inMessage
     * @brief 서버에서 전송한 Message 요청 메시지를 처리하는 메서드
     * @return
     */
    public boolean inMessage(HtpFormat htpFormat) {
        String messageType = htpFormat.getBody().get(HtpKey.CONFERENCE_ID);
        log.debug("!!!!! input type {}", messageType);
        switch (messageType){
            // connect 여부 확인
            case HtpType.CONNECT:
                outAccept(htpFormat);
                break;
            // 현재 방 목록 최신화
            case HtpKey.CONFERENCE_ID:
                instance.getClientGUI().getConfListTextArea().setText(htpFormat.getBody().get(HtpKey.TEXT));
                outAccept(htpFormat);
                break;
            // 전체 공지
            case HtpKey.TEXT:
                String preNoticeText = instance.getClientGUI().getLogTextArea().getText();
                instance.getClientGUI().getLogTextArea().setText(preNoticeText + htpFormat.getBody().get(HtpKey.TEXT) + "\n");
                outAccept(htpFormat);
                break;
            // 대화 conferenceId 불일치시 실패
            default:
                if (htpFormat.getBody().containsKey(HtpKey.CONFERENCE_ID) && htpFormat.getBody().get(HtpKey.CONFERENCE_ID).equals(instance.getConferenceId())) {
                    String preTalkText = instance.getClientGUI().getConferenceTextArea().getText();
                    instance.getClientGUI().getConferenceTextArea().setText(preTalkText + htpFormat.getBody().get(HtpKey.TEXT) + "\n");
                } else {
                    outDeny(htpFormat, "MESSAGE Processing Fail.");
                }
                break;
        }
        return true;
    }

    /**
     * @fn private boolean outAccept
     * @brief 요청 메시지에 대한 승인 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean outAccept(HtpFormat htpFormat){
        String message = HtpResponseMessage.createHtpAccept(htpFormat);

        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }

    /**
     * @fn private boolean outDeny
     * @brief 요청 메시지에 대한 거부 처리하는 메서드
     * @param htpFormat
     * @return
     */
    private boolean outDeny(HtpFormat htpFormat, String reason){
        String message = HtpResponseMessage.createHtpDeny(htpFormat, reason);

        NettyManager.getInstance().getNettyClient().send(message);
        return true;
    }
}
