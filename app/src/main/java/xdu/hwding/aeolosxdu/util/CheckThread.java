package xdu.hwding.aeolosxdu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.ArrayList;

import module.ECard;
import module.PhysicalExperiment;
import module.SportsClock;

public class CheckThread extends Thread{
    private String ID;
    private String spclk;
    private String phyexp;
    private String ecard_text;
    private String captcha;
    private Handler checkAccountHandler;
    private ECard eCard;
    private Context context;

    public CheckThread(String ID,
                String spclk,
                String phyexp,
                String ecard_text,
                String captcha,
                Handler checkAccountHandler,
                ECard eCard,
                Context context) {
        this.ID = ID;
        this.spclk = spclk;
        this.phyexp = phyexp;
        this.ecard_text = ecard_text;
        this.captcha = captcha;
        this.checkAccountHandler = checkAccountHandler;
        this.eCard = eCard;
        this.context = context;
    }

    public void run() {
        SportsClock sportsClock = new SportsClock();
        PhysicalExperiment physicalExperiment = new PhysicalExperiment();
        boolean spclk_ok;
        boolean phyexp_ok;
        boolean ecard_ok;
        try {
            spclk_ok = sportsClock.login(ID, spclk);
            phyexp_ok = physicalExperiment.login(ID, phyexp);
            ecard_ok = eCard.login(ID, ecard_text, captcha);

            if (spclk_ok && phyexp_ok && ecard_ok) {
                Message message = new Message();
                message.what = 0;
                message.obj = eCard;
                checkAccountHandler.sendMessage(message);

                ArrayList<String> stringArrayList = new ArrayList<>();
                stringArrayList.add(ID);
                stringArrayList.add(spclk);
                stringArrayList.add(phyexp);
                stringArrayList.add(ecard_text);
                storeAccountInfo(stringArrayList, context);
            }
            else
                checkAccountHandler.sendEmptyMessage(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeAccountInfo(ArrayList<String> stringArrayList, Context context) {
        SharedPreferences.Editor spe = context.getSharedPreferences("ACCOUNT_INFO", 0).edit();
        spe.putString("ID", stringArrayList.get(0))
           .putString("spclk", stringArrayList.get(1))
           .putString("phyexp", stringArrayList.get(2))
           .putString("ecard_text", stringArrayList.get(3));
        spe.apply();
    }
}
