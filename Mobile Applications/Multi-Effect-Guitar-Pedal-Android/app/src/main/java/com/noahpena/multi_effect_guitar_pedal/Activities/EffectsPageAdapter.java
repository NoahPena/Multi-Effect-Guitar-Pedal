package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by noah-pena on 11/28/16.
 */

public class EffectsPageAdapter extends FragmentStatePagerAdapter
{

    private int amountOfTabs = 1;

    private Context context;

    private int tabToBeUpdated = -1;

    private Fragment[] fragments;

    public EffectsPageAdapter(FragmentManager fm, Context context)
    {
        super(fm);

        this.context = context;

        this.fragments = new Fragment[3];
    }

    public EffectsFragment getFragment(int index)
    {
        return (EffectsFragment)fragments[index];
    }

    public void setAmountOfTabs(int amount)
    {
        amountOfTabs = amount;
        updateAllTabs();
    }

    public void updateAllTabs()
    {
        tabToBeUpdated = -2;
        notifyDataSetChanged();
    }

    public boolean addTab()
    {
        if(amountOfTabs < 3)
        {
            amountOfTabs++;
        }

        updateAllTabs();
        //tabToBeUpdated = amountOfTabs - 1;
        //notifyDataSetChanged();

        return (amountOfTabs < 3);
    }

    public boolean removeTab(int tabToRemove)
    {
        if(amountOfTabs > 1)
        {
            amountOfTabs--;
        }

        updateAllTabs();

//        tabToBeUpdated = amountOfTabs - 1;
//        notifyDataSetChanged();

        return (amountOfTabs > 1);
    }

    @Override
    public Fragment getItem(int position)
    {
        Bundle bundle = new Bundle();

        switch(position)
        {
            case 0:
                bundle.putString("Effect", UserPreferences.getTabOneEffect(context));
                bundle.putInt("SpinnerValue", UserPreferences.getTabOneSpinnerPosition(context));
                break;

            case 1:
                bundle.putString("Effect", UserPreferences.getTabTwoEffect(context));
                bundle.putInt("SpinnerValue", UserPreferences.getTabTwoSpinnerPosition(context));
                break;

            case 2:
                bundle.putString("Effect", UserPreferences.getTabThreeEffect(context));
                bundle.putInt("SpinnerValue", UserPreferences.getTabThreeSpinnerPosition(context));
                break;
        }

        bundle.putInt("TabNumber", position);

        Fragment fragment = new EffectsFragment();

        fragment.setArguments(bundle);

        fragments[position] = fragment;

        if(tabToBeUpdated == -2 && position == (amountOfTabs - 1))
        {
            tabToBeUpdated = -1;
        }

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

    public void updateTab(int position)
    {
        this.tabToBeUpdated = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)
    {
        if(tabToBeUpdated == -2)
        {
            return POSITION_NONE;
        }
        if(tabToBeUpdated == -1)
        {
            return POSITION_UNCHANGED;
        }
        else
        {
            if(((EffectsFragment)object).tabNumber == tabToBeUpdated)
            {
                Log.d("DEBUG", "CHANGED");
                tabToBeUpdated = -1;
                return POSITION_NONE;
            }
            else
            {
                return POSITION_UNCHANGED;
            }
        }
    }
}
