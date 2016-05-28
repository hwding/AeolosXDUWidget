package xdu.hwding.aeolosxdu;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;

import FooPackage.ECard;

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
                eCard_strs = eCard.queryTransferInfo("2016-04-21", "2016-05-21");
                views.setTextViewText(R.id.balance, "￥ "+eCard_strs.get(eCard_strs.size()-2));
                views.setTextViewText(R.id.latest_consumption, "￥ "+eCard_strs.get(eCard_strs.size()-3));
            } catch (IOException e) {
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

