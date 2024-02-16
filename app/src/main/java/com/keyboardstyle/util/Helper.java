package com.keyboardstyle.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class Helper {
    public static String fileBaseUrl = "http://applicanic.com/promotions/";
    public static Boolean isAdLoaded = Boolean.valueOf(false);
    public static Boolean isFromKeyboardSetting = Boolean.valueOf(false);

    public static void rate(Context context, String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(str);
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://play.google.com/store/apps/details?id=");
            sb2.append(str);
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb2.toString())));
        }
    }

    public static boolean isPackageInstalled(String str, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }

}
