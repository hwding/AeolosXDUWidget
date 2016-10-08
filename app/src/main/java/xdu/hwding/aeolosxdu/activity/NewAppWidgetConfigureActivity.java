package xdu.hwding.aeolosxdu.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import FooPackage.ECard;
import xdu.hwding.aeolosxdu.R;
import xdu.hwding.aeolosxdu.util.CaptchaLoaderThread;
import xdu.hwding.aeolosxdu.util.CheckThread;

public class NewAppWidgetConfigureActivity extends Activity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public NewAppWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        setContentView(R.layout.new_app_widget_configure);
        ImageView captchaImageView = (ImageView) findViewById(R.id.captcha);
        File file = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory()) +
                                File.separator +
                                "temp.jpeg");
        Handler handler = generateHandler(captchaImageView, file);
        try {
            CaptchaLoaderThread captchaLoaderThread = new CaptchaLoaderThread(file, handler);
            captchaLoaderThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener generateOnClickListener(final ECard eCard) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mID = (EditText) findViewById(R.id.ID);
                EditText mSportsClock = (EditText) findViewById(R.id.spclk);
                EditText mphyexp = (EditText) findViewById(R.id.phyexp);
                EditText ecard_text = (EditText) findViewById(R.id.ecard);
                EditText captcha = (EditText) findViewById(R.id.captcha_text);
                CheckThread checkThread = new CheckThread(
                        mID.getText().toString(),
                        mSportsClock.getText().toString(),
                        mphyexp.getText().toString(),
                        ecard_text.getText().toString(),
                        captcha.getText().toString(),
                        generateCheckAccountHandler(),
                        eCard,
                        getApplicationContext());
                checkThread.start();
            }
        };
    }

    Handler generateHandler(final ImageView imageView, final File file) {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(file));
                imageView.setImageBitmap(bitmap);
                findViewById(R.id.add_button).setEnabled(true);
                findViewById(R.id.add_button).setOnClickListener(generateOnClickListener((ECard) msg.obj));
            }
        };
    }

    Handler generateCheckAccountHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(NewAppWidgetConfigureActivity.this,
                                "所有账户关联成功",
                                Toast.LENGTH_LONG).show();
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(
                                NewAppWidgetConfigureActivity.this);
                        NewAppWidget.setProperties((ECard) msg.obj, getApplicationContext());
                        NewAppWidget.updateAppWidget(NewAppWidgetConfigureActivity.this,
                                appWidgetManager, mAppWidgetId);
                        Intent resultValue = new Intent();
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                        setResult(RESULT_OK, resultValue);
                        finish();
                        break;
                    case -1:
                        Toast.makeText(NewAppWidgetConfigureActivity.this,
                                "账户验证失败",
                                Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        };
    }
}

