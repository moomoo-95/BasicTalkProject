import org.junit.BeforeClass;
import org.junit.Test;

public class TestServer {

    private static final String PORT = "5120";

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void mainTest(){
        new Thread(() -> BasicTalkServerMain.main(new String[]{PORT})).start();

        try {
            while (true){
                Thread.sleep(1000);
            }
        } catch (Exception e){

        }
    }
}
