package moomoo.gui;

import moomoo.AppInstance;
import moomoo.core.htp.base.HtpType;
import moomoo.core.htp.process.HtpOutgoingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class ClientGUI extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(ClientGUI.class);

    private static final String CENTER = "Center";

    // notice 탭 내부 로그창, 공지 입력 란
    private final JTextArea logTextArea = new JTextArea(30, 30);
    private final JTextField noticeTextField = new JTextField(22);

    // roomList 탭 방 목록
    private final JTextArea confListTextArea = new JTextArea(30, 30);

    // room 탭 방
    private final JTextArea conferenceTextArea = new JTextArea(30, 30);

    // 등록 버튼
    private JButton noticeButton;

    public ClientGUI(String title) throws HeadlessException {
        super(title);

        // 프레임 크기
        setSize(400, 600);
        setBounds(0, 0, 400, 600);
        // 화면 가운데 배치
        setLocationRelativeTo(null);
        // 닫을 때 메모리에서 제거되도록 설정
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 크기 고정
        setResizable(false);
        // 보이게 설정
        setVisible(true);

        // 탭에 들어갈 panel 세팅
        JPanel noticePanel = createNoticePanel();
        JPanel userPanel = createConfListPanel();
        JPanel conferencePanel = createConferencePanel();
        // 탭 세팅
        JTabbedPane jTabbedPane = new JTabbedPane();
        add(jTabbedPane);

        jTabbedPane.addTab("Notice", noticePanel);
        jTabbedPane.addTab("Conference List", userPanel);
        jTabbedPane.addTab("Conference", conferencePanel);

        jTabbedPane.setBackgroundAt(0, Color.GRAY);
        jTabbedPane.setBackgroundAt(1, Color.GRAY);
        jTabbedPane.setBackgroundAt(2, Color.GRAY);

        add(jTabbedPane);

    }

    private JPanel createNoticePanel(){
        JPanel noticePanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        noticePanel.setLayout(flowLayout);

        // 로그 화면
        JPanel logPanel = new JPanel(new BorderLayout());
        logTextArea.setEditable(false);

        // 스크롤
        JScrollPane jScrollPane = new JScrollPane(logTextArea);
        jScrollPane.createVerticalScrollBar();
        jScrollPane.createHorizontalScrollBar();
        logPanel.add(jScrollPane, CENTER);
        noticePanel.add(logPanel);

        // 등록 입력 필드
        noticeTextField.setText("");
        noticePanel.add(noticeTextField);

        // 등록 버튼
        noticeButton = new JButton(HtpType.CONNECT);
        noticeButton.addActionListener(new ConnectListener());
        noticeButton.setEnabled(true);
        noticePanel.add(noticeButton);

        noticePanel.setPreferredSize(new Dimension(380, 100));
        this.add(noticePanel, CENTER);

        return noticePanel;
    }

    private JPanel createConfListPanel(){
        JPanel confPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        confPanel.setLayout(flowLayout);

        // 로그 화면
        JPanel confListPanel = new JPanel(new BorderLayout());
        confListTextArea.setEditable(false);

        // 스크롤
        JScrollPane jScrollPane = new JScrollPane(confListTextArea);
        jScrollPane.createVerticalScrollBar();
        jScrollPane.createHorizontalScrollBar();
        confListPanel.add(jScrollPane, CENTER);
        confPanel.add(confListPanel);

        confPanel.setPreferredSize(new Dimension(380, 100));
        this.add(confPanel, CENTER);

        return confPanel;
    }

    private JPanel createConferencePanel(){
        JPanel conferencePanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        conferencePanel.setLayout(flowLayout);

        // 로그 화면
        JPanel conferenceTalkPanel = new JPanel(new BorderLayout());
        conferenceTextArea.setEditable(false);

        // 스크롤
        JScrollPane jScrollPane = new JScrollPane(conferenceTextArea);
        jScrollPane.createVerticalScrollBar();
        jScrollPane.createHorizontalScrollBar();
        conferenceTalkPanel.add(jScrollPane, CENTER);
        conferencePanel.add(conferenceTalkPanel);

        conferencePanel.setPreferredSize(new Dimension(380, 100));
        this.add(conferencePanel, CENTER);

        return conferencePanel;
    }

    class ConnectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            log.debug("{} Button Click", e.getActionCommand());
            // CONNECT 일경우 옆 input 에 적인 String 값을 userName 으로 사용하여 Connect
            if (!noticeTextField.getText().equals("") && noticeButton.getText().equals(HtpType.CONNECT)) {
                AppInstance.getInstance().setUserName(noticeTextField.getText());

                new HtpOutgoingMessage().outConnect();

                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String noticeLog = logTextArea.getText() + dateFormat.format(System.currentTimeMillis()) + " Name : " + noticeTextField.getText() + " connect.\n";
                logTextArea.setText(noticeLog);
                noticeTextField.setText("");
                noticeTextField.grabFocus();

                noticeButton.setText(HtpType.DISCONNECT);

            }
            // DISCONNECT 일 경우 연결 해제
            else if (noticeButton.getText().equals(HtpType.DISCONNECT)) {
                new HtpOutgoingMessage().outDisconnect();
                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String noticeLog = logTextArea.getText() + dateFormat.format(System.currentTimeMillis()) + " Name : " + AppInstance.getInstance().getUserName() + " disconnect.\n";
                logTextArea.setText(noticeLog);

                noticeButton.setText(HtpType.CONNECT);
            }

        }


    }

    public JTextArea getLogTextArea() { return logTextArea; }

    public JTextArea getConfListTextArea() { return confListTextArea; }

    public JTextArea getConferenceTextArea() { return conferenceTextArea; }
}
