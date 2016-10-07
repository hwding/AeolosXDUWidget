package xdu.hwding.aeolosxdu;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.widget.RemoteViews;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import FooPackage.ECard;
import FooPackage.PhysicalExperiment;
import FooPackage.SportsClock;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NewAppWidgetConfigureActivity NewAppWidgetConfigureActivity}
 */
public class NewAppWidget extends AppWidgetProvider {

    static ECard eCard;
    static SharedPreferences sharedPreferences;
    static boolean isVerified = false;
    static ArrayList<String> eCard_strs;

    static void setProperties(ECard eCard,
                              Context context) {

        NewAppWidget.eCard = eCard;
        sharedPreferences = context.getSharedPreferences("ACCOUNT_INFO", 0);
        isVerified = true;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        // Instruct the widget manager to update the widget
        if (isVerified) {
            try {
                Calendar calendar = GregorianCalendar.getInstance();
                Calendar calendarLastMonth = GregorianCalendar.getInstance();
                calendarLastMonth.add(Calendar.MONTH, -1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                eCard_strs = eCard.queryTransferInfo(
                        simpleDateFormat.format(calendarLastMonth.getTime()),
                        simpleDateFormat.format(calendar.getTime()));
                views.setTextViewText(R.id.balance, "￥ "+eCard_strs.get(4));
                views.setTextViewText(R.id.latest_consumption, "￥ "+eCard_strs.get(3) + " @ " +eCard_strs.get(0));
                SportsClock sportsClock = new SportsClock();
                sportsClock.login(
                        sharedPreferences.getString("ID", ""),
                        sharedPreferences.getString("spclk", ""));
                ArrayList<String> stringArrayList = null;
                if (sportsClock.checkIsLogin(sharedPreferences.getString("ID", "")))
                    stringArrayList = sportsClock.queryAchievements();
                if (stringArrayList != null) {
                    views.setTextViewText(R.id.latest_clock, stringArrayList.get(stringArrayList.size()-4));
                    views.setTextViewText(R.id.total_clocks, String.valueOf(stringArrayList.size()/5));
                }
                PhysicalExperiment physicalExperiment = new PhysicalExperiment();

                ArrayList<String> stringArrayList1;
                if (physicalExperiment.login(
                        sharedPreferences.getString("ID", ""),
                        sharedPreferences.getString("phyexp", "")
                )) {
                    stringArrayList1 = physicalExperiment.queryAchievements();
                    if (stringArrayList1.size()!=0) {
                        boolean FLAG_le_SET = false;
                        boolean FLAG_ne_SET = false;
                        int latestExpNum = stringArrayList1.size()/10-1;
                        int nextExpNum = -1;
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM/dd/yyyy", Locale.CHINA);
                        Date date = new Date();
                        for (int i=0; i<(stringArrayList1.size()/10); i++) {
                            Date THIS = simpleDateFormat1.parse(stringArrayList1.get(10*i+5-1));
                            if (!FLAG_le_SET)
                                if ("未录入".equals(stringArrayList1.get(10*i+8-1))) {
                                    latestExpNum = i - 1;
                                    FLAG_le_SET = true;
                                }
                            if (!FLAG_ne_SET)
                                if (!THIS.before(date)) {
                                    nextExpNum = i;
                                    FLAG_ne_SET = true;
                                }
                        }

                        Date nextTime = simpleDateFormat1.parse(stringArrayList1.get(10*nextExpNum+5-1));
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEEE",Locale.CHINA);
                        String weekday = simpleDateFormat2.format(nextTime);

                        views.setTextViewText(R.id.latest_exp, stringArrayList1.get(10*latestExpNum+2-1));
                        views.setTextViewText(R.id.latest_score, stringArrayList1.get(10*latestExpNum+8-1));
                        views.setTextViewText(R.id.next_exp, stringArrayList1.get(10*nextExpNum+2-1));
                        views.setTextViewText(R.id.location, stringArrayList1.get(10*nextExpNum+6-1));
                        views.setTextViewText(R.id.next_time, stringArrayList1.get(10*nextExpNum+5-1) + "  " + weekday);
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context,
                          int[] appWidgetIds) {

        // When the user deletes the widget, delete the preference associated with it.
//        for (int appWidgetId : appWidgetIds) {
//            NewAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

