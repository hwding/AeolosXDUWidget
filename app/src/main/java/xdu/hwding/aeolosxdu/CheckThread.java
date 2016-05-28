package xdu.hwding.aeolosxdu;

import android.os.Handler;
import java.io.IOException;

import FooPackage.ECard;
import FooPackage.PhysicalExperiment;
import FooPackage.SportsClock;

public class CheckThread extends Thread{
    String ID;
    String spclk;
    String phyexp;
    String ecard_text;
    String captcha;
    Handler checkAccountHandler;
    ECard eCard;

    CheckThread(String ID, String spclk, String phyexp, String ecard_text, String captcha,
                Handler checkAccountHandler, ECard eCard) {
        this.ID = ID;
        this.spclk = spclk;
        this.phyexp = phyexp;
        this.ecard_text = ecard_text;
        this.captcha = captcha;
        this.checkAccountHandler = checkAccountHandler;
        this.eCard = eCard;
    }

    public void run() {
        SportsClock sportsClock = new SportsClock();
        PhysicalExperiment physicalExperiment = new PhysicalExperiment();
        try {
            System.out.println(sportsClock.checkIsLogin(
                    sportsClock.login(ID, spclk)
            ));
            System.out.println(physicalExperiment.login(ID, phyexp));
            eCard.login(captcha, ID, ecard_text);
            System.out.println(eCard.checkIsLogin(ID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
