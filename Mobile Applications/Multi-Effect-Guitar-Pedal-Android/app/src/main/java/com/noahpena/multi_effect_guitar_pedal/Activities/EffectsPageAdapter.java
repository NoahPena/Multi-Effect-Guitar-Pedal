package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by noah-pena on 11/28/16.
 */

public class EffectsPageAdapter extends FragmentStatePagerAdapter
{

    private int amountOfTabs = 2;

    private Context context;

    public EffectsPageAdapter(FragmentManager fm, Context context)
    {
        super(fm);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        Bundle bundle = new Bundle();

        switch(position)
        {
            case 0:
                bundle.putString("Effect", UserPreferences.getTabOneEffect(context));
                break;

            case 1:
                bundle.putString("Effect", "Reverb");
                //bundle.putString("Effect", UserPreferences.getTabTwoEffect(context));
                break;

            case 2:
                bundle.putString("Effect", UserPreferences.getTabThreeEffect(context));
                break;
        }

        Fragment fragment = new EffectsFragment();

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount()
    {
        return amountOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        // Generate title based on item position
        switch(position)
        {
            case 0:
                return UserPreferences.getTabOneEffect(context);

            case 1:
                return UserPreferences.getTabTwoEffect(context);

            case 2:
                return UserPreferences.getTabThreeEffect(context);

            default:
                return "ERROR";
        }
    }


}
