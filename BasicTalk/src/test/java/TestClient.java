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
            while (true){
                Thread.sleep(1000);
            }
        } catch (Exception e){

        }
    }
}
