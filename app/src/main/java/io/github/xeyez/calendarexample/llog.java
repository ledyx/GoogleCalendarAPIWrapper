package io.github.xeyez.calendarexample;

import android.util.Log;

/**
 * Created by Administrator on 2017-05-26.
 */

public class llog {
    private llog() {
    }

    public static void d(Exception e) {
        Log.d(getTag(e), e.getMessage());
    }

    public static void v(Exception e) {
        Log.v(getTag(e), e.getMessage());
    }

    public static void i(Exception e) {
        Log.i(getTag(e), e.getMessage());
    }

    public static void w(Exception e) {
        Log.w(getTag(e), e.getMessage());
    }

    public static void e(Exception e) {
        Log.e(getTag(e), e.getMessage());
    }

    private static String getTag(Exception e) {
        StackTraceElement ste = e.getStackTrace()[0];
        StringBuilder sb1 = new StringBuilder();
        sb1.append(ste.getClassName());
        sb1.append(".");
        sb1.append(ste.getMethodName());
        sb1.append("() ");
        sb1.append(ste.getLineNumber());

        return sb1.toString();
    }
}