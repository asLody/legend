package com.legend.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.lody.legend.Hook;
import com.lody.legend.HookManager;

/**
 * @author Lody
 * @version 1.0
 */
public class App extends Application {

    public static boolean ENABLE_TOAST = true;
    public static boolean ALLOW_LAUNCH_ACTIVITY = true;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HookManager.getDefault().applyHooks(App.class);

    }

    @Hook("android.app.Application::onCreate")
    public static void Application_onCreate(Application app) {
        Toast.makeText(app, "Application => onCreate()", Toast.LENGTH_SHORT).show();
        HookManager.getDefault().callSuper(app);
    }


    @Hook("android.telephony.TelephonyManager::getSimSerialNumber")
    public static String TelephonyManager_getSimSerialNumber(TelephonyManager thiz) {
         return "110";
    }


    @Hook("android.widget.Toast::show")
    public static void Toast_show(Toast toast) {
        if (ENABLE_TOAST) {
            HookManager.getDefault().callSuper(toast);
        }
    }

    @Hook("android.app.Activity::startActivity@android.content.Intent")
    public static void Activity_startActivity(Activity activity, Intent intent) {
        if (!ALLOW_LAUNCH_ACTIVITY) {
            Toast.makeText(activity, "I am sorry to turn your Activity down :)", Toast.LENGTH_SHORT).show();
        }else {
            HookManager.getDefault().callSuper(activity, intent);
        }
    }

}
