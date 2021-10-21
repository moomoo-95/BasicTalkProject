import moomoo.AppInstance;
import moomoo.BasicTalkMain;
import moomoo.netty.NettyManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestClient {

    private static final String HOST = "192.168.2.163";
    private static final String PORT = String.valueOf(AppInstance.PORT);

    private static final String USER_ID = "HTP_"+(int)(Math.random()*10000000);
    private static final String USER_NAME = "hyeonseong Lim";

    private static final String REMOTE_HOST = AppInstance.REMOTE_IP;
    private static final String REMOTE_PORT = String.valueOf(AppInstance.REMOTE_PORT);

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void mainTest(){
        new Thread(() -> BasicTalkMain.main(new String[]{HOST, PORT})).start();

        try {
            while (true){
                Thread.sleep(1000);
            }
        } catch (Exception e){

        }
    }
}
