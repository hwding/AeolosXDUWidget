package xdu.hwding.aeolosxdu;

import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.IOException;
import FooPackage.ECard;

public class CaptchaLoaderThread extends Thread{
    File file;
    ECard eCard;
    Handler handler;

    CaptchaLoaderThread(File file, Handler handler) throws IOException {
        this.file = file;
        this.handler = handler;
    }

    public void run() {
        try {
            eCard = new ECard();
            eCard.getCaptcha(file);
            Message message = new Message();
            message.obj = eCard;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
