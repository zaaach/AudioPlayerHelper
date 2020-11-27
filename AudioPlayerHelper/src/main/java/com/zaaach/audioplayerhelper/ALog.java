package com.zaaach.audioplayerhelper;

import android.util.Log;

/**
 * @Author: Zaaach
 * @Date: 2020/11/27
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class ALog {
    public static boolean DEBUG = false;

    public static void d(String tag, String message){
        if (DEBUG) {
            Log.d(tag, message);
        }
    }
}
