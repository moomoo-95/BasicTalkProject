package moomoo.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ServerGUI extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(ServerGUI.class);

    // notice 탭 내부 로그창, 공지 입력 란
    private final JTextArea logTextArea = new JTextArea(30, 30);
    private final JTextField noticeTextField = new JTextField(22);

    // user 탭 유저 목록
    private final JTextArea userTextArea = new JTextArea(30, 30);

    // room 탭 방 목록
    private final JTextArea roomTextArea = new JTextArea(30, 30);
    public ServerGUI(String title) throws HeadlessException {
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
        JPanel userPanel = createUserPanel();
        JPanel roomPanel = createRoomPanel();
        // 탭 세팅
        JTabbedPane jTabbedPane = new JTabbedPane();
        add(jTabbedPane);

        jTabbedPane.addTab("Notice", noticePanel);
        jTabbedPane.addTab("User", userPanel);
        jTabbedPane.addTab("Room", roomPanel);

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
        logPanel.add(jScrollPane, "Center");
        noticePanel.add(logPanel);

        // 공지 입력 필드
        noticeTextField.setText("");
        noticePanel.add(noticeTextField);

        // 공지 버튼
        JButton noticeButton = new JButton("NOTICE");
        noticeButton.addActionListener(new NoticeListener());
        noticeButton.setEnabled(true);
        noticePanel.add(noticeButton);

        noticePanel.setPreferredSize(new Dimension(380, 100));
        this.add(noticePanel, "Center");

        return noticePanel;
    }

    private JPanel createUserPanel(){
        JPanel userPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        userPanel.setLayout(flowLayout);

        // 로그 화면
        JPanel userListPanel = new JPanel(new BorderLayout());
        userTextArea.setEditable(false);

        // 스크롤
        JScrollPane jScrollPane = new JScrollPane(userTextArea);
        jScrollPane.createVerticalScrollBar();
        jScrollPane.createHorizontalScrollBar();
        userListPanel.add(jScrollPane, "Center");
        userPanel.add(userListPanel);

        userPanel.setPreferredSize(new Dimension(380, 100));
        this.add(userPanel, "Center");

        return userPanel;
    }

    private JPanel createRoomPanel(){
        JPanel roomPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        roomPanel.setLayout(flowLayout);

        // 로그 화면
        JPanel roomListPanel = new JPanel(new BorderLayout());
        roomTextArea.setEditable(false);

        // 스크롤
        JScrollPane jScrollPane = new JScrollPane(roomTextArea);
        jScrollPane.createVerticalScrollBar();
        jScrollPane.createHorizontalScrollBar();
        roomListPanel.add(jScrollPane, "Center");
        roomPanel.add(roomListPanel);

        roomPanel.setPreferredSize(new Dimension(380, 100));
        this.add(roomPanel, "Center");

        return roomPanel;
    }

    class NoticeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            log.debug("{} Button Click", e.getActionCommand());
            if (!noticeTextField.getText().equals("")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
                String log = logTextArea.getText() + dateFormat.format(System.currentTimeMillis()) + " Notice : " + noticeTextField.getText() + "\n";
                logTextArea.setText(log);
                noticeTextField.setText("");
                noticeTextField.grabFocus();
            }

        }


    }
}
