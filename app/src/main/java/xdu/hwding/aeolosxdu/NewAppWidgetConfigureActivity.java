package xdu.hwding.aeolosxdu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import FooPackage.ECard;


public class NewAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "xdu.hwding.aeolosxdu.NewAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    public NewAppWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.new_app_widget_configure);
        ImageView captchaImageView = (ImageView) findViewById(R.id.captcha);
        File file = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory()) +
                                File.separator + "temp.jpeg");
        Handler handler = generateHandler(captchaImageView, file);
        try {
            CaptchaLoaderThread captchaLoaderThread = new CaptchaLoaderThread(file, handler);
            captchaLoaderThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            final Context context = NewAppWidgetConfigureActivity.this;
//
//            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);
//
//            // It is the responsibility of the configuration activity to update the app widget
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            NewAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
//
//            // Make sure we pass back the original appWidgetId
//            Intent resultValue = new Intent();
//            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//            setResult(RESULT_OK, resultValue);
//            finish();
//        }
//    };

    View.OnClickListener generateOnClickListener(ECard eCard) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mID = (EditText) findViewById(R.id.ID);
                EditText mSportsClock = (EditText) findViewById(R.id.spclk);
                EditText mphyexp = (EditText) findViewById(R.id.phyexp);
                CheckThread checkThread = new CheckThread(mID.getText().toString(),
                        mSportsClock.getText().toString(),
                        mphyexp.getText().toString(),
                        generateCheckAccountHandler());
                checkThread.start();
            }
        };
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
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

            }
        };
    }
}

