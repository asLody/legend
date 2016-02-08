package com.lody.legend.utility;

import android.util.Log;

/**
 * @author Lody
 * @version 1.0
 */
public class Logger {

    private static final String TAG = "Legend-Log";

    public static boolean OPEN_LOG = true;

    public static void i(String msg) {
        if (OPEN_LOG) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String format, Object... args) {
        i(String.format(format, args));
    }

    public static void d(String msg) {
        if (OPEN_LOG) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String format, Object... args) {
        d(String.format(format, args));
    }

    public static void w(String msg) {
        if (OPEN_LOG) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String format, Object... args) {
        w(String.format(format, args));
    }

    public static void e(String msg) {
        if (OPEN_LOG) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String format, Object... args) {
        e(String.format(format, args));
    }
}
