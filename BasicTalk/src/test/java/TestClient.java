import moomoo.BasicTalkMain;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestClient {

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void mainTest(){
        new Thread(() -> BasicTalkMain.main(new String[]{})).start();

        try {
            while (true){
                Thread.sleep(1000);
            }
        } catch (Exception e){

        }
    }

    @Test
    public void mainTest2(){
        new Thread(() -> BasicTalkMain.main(new String[]{})).start();

        try {
            while (true){
                Thread.sleep(1000);
            }
        } catch (Exception e){

        }
    }
}
