package xdu.hwding.aeolosxdu;

import android.os.Handler;
import java.io.IOException;
import FooPackage.PhysicalExperiment;
import FooPackage.SportsClock;

public class CheckThread extends Thread{
    String ID;
    String spclk;
    String phyexp;
    Handler checkAccountHandler;

    CheckThread(String ID, String spclk, String phyexp, Handler checkAccountHandler) {
        this.ID = ID;
        this.spclk = spclk;
        this.phyexp = phyexp;
        this.checkAccountHandler = checkAccountHandler;
    }

    public void run() {
        SportsClock sportsClock = new SportsClock();
        PhysicalExperiment physicalExperiment = new PhysicalExperiment();
        try {
            System.out.println(sportsClock.checkIsLogin(
                    sportsClock.login(ID, spclk)
            ));
            System.out.println(physicalExperiment.login(ID, phyexp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
