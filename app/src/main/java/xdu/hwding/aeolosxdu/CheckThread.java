package xdu.hwding.aeolosxdu;

import android.os.Handler;
import android.os.Message;
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

    CheckThread(String ID,
                String spclk,
                String phyexp,
                String ecard_text,
                String captcha,
                Handler checkAccountHandler,
                ECard eCard) {
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
        boolean spclk_ok;
        boolean phyexp_ok;
        boolean ecard_ok;
        try {
            spclk_ok = sportsClock.checkIsLogin(sportsClock.login(ID, spclk));
            phyexp_ok = physicalExperiment.login(ID, phyexp);
            eCard.login(captcha, ID, ecard_text);
            ecard_ok = eCard.checkIsLogin(ID);
            System.out.println(ecard_ok);

            if (spclk_ok && phyexp_ok && ecard_ok) {
                Message message = new Message();
                message.what = 0;
                checkAccountHandler.sendMessage(message);
            }
            else if (!spclk_ok) {
                Message message_spclk = new Message();
                message_spclk.what = -1;
                checkAccountHandler.sendMessage(message_spclk);
            }
            else if (!phyexp_ok) {
                Message message_phyexp = new Message();
                message_phyexp.what = -2;
                checkAccountHandler.sendMessage(message_phyexp);
            }
            else if (!ecard_ok) {
                Message message_ecard = new Message();
                message_ecard.what = -3;
                checkAccountHandler.sendMessage(message_ecard);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
