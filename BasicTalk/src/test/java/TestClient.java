import moomoo.BasicTalkMain;
import moomoo.netty.NettyManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestClient {

    private static final String  HOST = "127.0.0.1";
    private static final String PORT = "5120";

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void mainTest(){
        new Thread(() -> BasicTalkMain.main(new String[]{HOST, PORT})).start();

        try {
            int cnt = 0;
            while (true){
                cnt++;
                Thread.sleep(1000);
                if(cnt%5 == 0){
                    NettyManager.getInstance().getNettyClient().send(200, "Hi this is test "+cnt);
                }
            }
        } catch (Exception e){

        }
    }
}
