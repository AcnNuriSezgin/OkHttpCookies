package nurisezgin.com.android.okhttpcookies.instestutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import nurisezgin.com.android.okhttpcookies.CookieInterceptorBuilder;
import nurisezgin.com.android.okhttpcookies.CookieUtil;

/**
 * Created by nuri on 25.07.2018
 */
public class InsTestUtils {

    private static final String HOST = "localhost";

    public static String getSharedPrefKey() {
        return HOST + CookieUtil.getCOOKIES_KEY();
    }

    public static HashSet<String> getStringSetFromSharedPref(Context ctx) {
        return (HashSet<String>) getSharedPref(ctx).getStringSet(getSharedPrefKey(), new HashSet<>());
    }

    public static void putStringSetToSharedPref(Context ctx, Set<String> value) {
        getSharedPref(ctx).edit().putStringSet(getSharedPrefKey(), value).apply();
    }

    public static void clearSharedPref(Context ctx) {
        getSharedPref(ctx).edit().clear().apply();
    }

    public static SharedPreferences getSharedPref(Context ctx) {
        return ctx.getSharedPreferences(
                CookieInterceptorBuilder.getPREF_NAME(), Context.MODE_PRIVATE);
    }
}
