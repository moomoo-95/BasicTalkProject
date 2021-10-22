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

    // notice 탭 내부 로그창, userName 입력 란, 등록/해제 버튼
    private final JTextArea logTextArea = new JTextArea(28, 28);
    private final JTextField connectTextField = new JTextField(19);
    private JButton connectButton;

    // roomList 탭 방, 목록 방 입력 란, 입장/퇴장 버튼
    private final JTextArea confListTextArea = new JTextArea(28, 28);
    private final JTextField enterTextField = new JTextField(22);
    private JButton enterButton;

    // room 탭 방, 대화 입력 란, 전송 버튼
    private final JTextArea conferenceTextArea = new JTextArea(28, 28);
    private final JTextField sendTextField = new JTextField(22);
    private JButton sendButton;

    public ClientGUI(String title) throws HeadlessException {
        super(title);

        // 프레임 크기
        setSize(600, 800);
        setBounds(0, 0, 400, 600);
        // 화면 가운데 배치
        setLocationRelativeTo(null);
        // 닫을 때 메모리에서 제거되도록 설정
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 크기 고정
        setResizable(false);

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

        // 보이게 설정
        setVisible(true);

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

        logPanel.setPreferredSize(new Dimension(this.getWidth()-30, this.getHeight()-120));
        noticePanel.add(logPanel);

        // 등록 입력 필드
        connectTextField.setText("");
        noticePanel.add(connectTextField);

        // 등록/해제 버튼
        connectButton = new JButton(HtpType.CONNECT);
        connectButton.addActionListener(new ConnectListener());
        connectButton.setEnabled(true);
        noticePanel.add(connectButton);

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

        confListPanel.setPreferredSize(new Dimension(this.getWidth()-30, this.getHeight()-120));
        confPanel.add(confListPanel);

        // 등록 입력 필드
        enterTextField.setText("");
        confPanel.add(enterTextField);

        // 등록/해제 버튼
        enterButton = new JButton(HtpType.ENTER);
        enterButton.addActionListener(new EnterListener());
        enterButton.setEnabled(true);
        confPanel.add(enterButton);

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

        conferenceTalkPanel.setPreferredSize(new Dimension(this.getWidth()-30, this.getHeight()-120));
        conferencePanel.add(conferenceTalkPanel);

        // 대화 입력 필드
        sendTextField.setText("");
        conferencePanel.add(sendTextField);

        // 전송 버튼
        sendButton = new JButton("SEND");
        sendButton.addActionListener(new SendListener());
        sendButton.setEnabled(true);
        conferencePanel.add(sendButton);

        this.add(conferencePanel, CENTER);

        return conferencePanel;
    }

    class ConnectListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            log.debug("{} Button Click", e.getActionCommand());
            // CONNECT 일경우 옆 input 에 적인 String 값을 userName 으로 사용하여 Connect
            if (!connectTextField.getText().equals("") && connectButton.getText().equals(HtpType.CONNECT)) {
                AppInstance.getInstance().setUserName(connectTextField.getText());

                new HtpOutgoingMessage().outConnect();

                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String noticeLog = logTextArea.getText() + dateFormat.format(System.currentTimeMillis()) + " Name : " + connectTextField.getText() + " connect.\n";
                logTextArea.setText(noticeLog);
                connectTextField.setText("");
                connectTextField.grabFocus();

                connectButton.setText(HtpType.DISCONNECT);

            }
            // DISCONNECT 일 경우 연결 해제
            else if (connectButton.getText().equals(HtpType.DISCONNECT)) {
                new HtpOutgoingMessage().outDisconnect();
                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String noticeLog = logTextArea.getText() + dateFormat.format(System.currentTimeMillis()) + " Name : " + AppInstance.getInstance().getUserName() + " disconnect.\n";
                logTextArea.setText(noticeLog);

                connectButton.setText(HtpType.CONNECT);
            }
        }
    }

    class EnterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            log.debug("{} Button Click", e.getActionCommand());
            // ENTER 일경우 옆 input 에 적인 String 값을 conferenceId 으로 사용하여 ENTER
            if (!enterTextField.getText().equals("") && enterButton.getText().equals(HtpType.ENTER)) {
                AppInstance.getInstance().setConferenceId(enterTextField.getText());

                new HtpOutgoingMessage().outEnter(enterTextField.getText());

                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String noticeLog = logTextArea.getText() + dateFormat.format(System.currentTimeMillis()) + " Room : " + enterTextField.getText() + " enter.\n";
                logTextArea.setText(noticeLog);
                enterTextField.setText("");
                enterTextField.grabFocus();

                enterButton.setText(HtpType.EXIT);

            }
            // EXIT 일 경우 퇴장
            else if (enterButton.getText().equals(HtpType.EXIT)) {
                new HtpOutgoingMessage().outExit();
                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String noticeLog = logTextArea.getText() + dateFormat.format(System.currentTimeMillis()) + " Room : " + AppInstance.getInstance().getUserName() + " exit.\n";
                logTextArea.setText(noticeLog);
                conferenceTextArea.setText("");

                enterButton.setText(HtpType.ENTER);
            }
        }
    }

    class SendListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            log.debug("{} Button Click", e.getActionCommand());

            if (!enterTextField.getText().equals("")) {


                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String sendMsg = dateFormat.format(System.currentTimeMillis()) + " "+ AppInstance.getInstance().getUserName() +" : " + sendTextField.getText() + "\n";
                new HtpOutgoingMessage().outMessage(sendMsg);

                sendTextField.setText("");
                sendTextField.grabFocus();
            }
        }
    }

    public JTextArea getLogTextArea() { return logTextArea; }

    public JTextArea getConfListTextArea() { return confListTextArea; }

    public JTextArea getConferenceTextArea() { return conferenceTextArea; }
}
