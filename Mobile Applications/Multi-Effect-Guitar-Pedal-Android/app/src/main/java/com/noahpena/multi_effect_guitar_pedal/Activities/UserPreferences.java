package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by noah-pena on 11/28/16.
 */

public class UserPreferences
{

    private static String APP_IDENTIFIER = "GUCCI";

    public static void setTabOneEffect(Context context, String effect, int spinnerPostion)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        sp.edit().putString("TabOneEffect", effect).apply();
        sp.edit().putInt("SpinnerOnePosition", spinnerPostion).apply();
    }

    public static String getTabOneEffect(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        return sp.getString("TabOneEffect", null);
    }

    public static int getTabOneSpinnerPosition(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        return sp.getInt("SpinnerOnePosition", 0);
    }

    public static void setTabTwoEffect(Context context, String effect, int spinnerPosition)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        sp.edit().putString("TabTwoEffect", effect).apply();
        sp.edit().putInt("SpinnerTwoPosition", spinnerPosition).apply();
    }

    public static String getTabTwoEffect(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        return sp.getString("TabTwoEffect", null);
    }

    public static int getTabTwoSpinnerPosition(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        return sp.getInt("SpinnerTwoPosition", 0);
    }

    public static void setTabThreeEffect(Context context, String effect, int spinnerPosition)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        sp.edit().putString("TabThreeEffect", effect).apply();
        sp.edit().putInt("SpinnerThreePosition", spinnerPosition).apply();
    }

    public static String getTabThreeEffect(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        return sp.getString("TabThreeEffect", null);
    }

    public static int getTabThreeSpinnerPosition(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE);
        return sp.getInt("SpinnerThreePosition", 0);
    }
}
