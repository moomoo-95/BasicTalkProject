import moomoo.AppInstance;
import moomoo.BasicTalkMain;
import moomoo.netty.NettyManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestClient {

    private static final String HOST = "127.0.0.1";
    private static final String PORT = String.valueOf(AppInstance.PORT);

    private static final String USER_ID = "HTP_"+(int)(Math.random()*10000000);
    private static final String USER_NAME = "hyeonseong Lim";

    private static final String REMOTE_HOST = AppInstance.REMOTE_IP;
    private static final String REMOTE_PORT = String.valueOf(AppInstance.REMOTE_PORT);

    // CONNECT DISCONNECT ENTER EXIT MESSAGE
    private static final String HTP_CONNECT_BODY =
            "userId=" + USER_ID + "\n" +
            "userName=" + USER_NAME + "\n";

    private static final String HTP_CONNECT_HEADER =
            "HTP/1.0 CONNECT\n" +
            "FROM: " + HOST + ":" + PORT + "\n" +
            "TO: " + REMOTE_HOST + ":" + REMOTE_PORT + "\n" +
            "LENGTH: " + HTP_CONNECT_BODY.length() + "\n\n"
            ;
    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void mainTest(){
        new Thread(() -> BasicTalkMain.main(new String[]{HOST, PORT})).start();

        String message = HTP_CONNECT_HEADER + HTP_CONNECT_BODY;
        try {
            int cnt = 0;
            while (true){
                cnt++;
                Thread.sleep(1000);
                if(cnt%5 == 0){
                    NettyManager.getInstance().getNettyClient().send(message);
                }
            }
        } catch (Exception e){

        }
    }
}
