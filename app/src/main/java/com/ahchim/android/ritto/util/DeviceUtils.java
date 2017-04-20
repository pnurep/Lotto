package com.ahchim.android.ritto.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Gold on 2017. 4. 19..
 */

public class DeviceUtils {

    public static int getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }
}
