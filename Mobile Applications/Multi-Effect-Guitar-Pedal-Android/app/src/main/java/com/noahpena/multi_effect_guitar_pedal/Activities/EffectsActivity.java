package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by noah-pena on 11/6/16.
 */

public class EffectsActivity extends AppCompatActivity
{

    Toolbar toolbar;
    Spinner spinner;
    public static ViewPager viewPager;
    TextView effectName;

    public static int tabSelected = 0;
    boolean manuallySelected = false;
    boolean tabManuallySelected = false;

    EffectsPageAdapter effectsPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effects_layout);

        Bluetooth.init(this);
        EffectsManager.init(this.getApplicationContext());

        spinner = (Spinner)findViewById(R.id.effectsSpinner);

        effectName = (TextView)findViewById(R.id.userEffectNameTextBox);

        toolbar = (Toolbar)findViewById(R.id.effectsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bluetooth.close();
                finish();
            }
        });

        Log.d("DEBUG", spinner.getSelectedItem().toString());

        effectsPageAdapter = new EffectsPageAdapter(getSupportFragmentManager(), getApplicationContext());

        viewPager = (ViewPager)findViewById(R.id.effectsViewPager);
        viewPager.setAdapter(effectsPageAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                manuallySelected = false;
                Log.d("DEBUG", "SWIPED");
            }
        });

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tabManuallySelected)
                {
                    tabManuallySelected = false;
                    return;
                }

                tabSelected = tab.getPosition();

                switch(tabSelected)
                {
                    case 0:
                        spinner.setSelection(UserPreferences.getTabOneSpinnerPosition(getApplicationContext()));
                        break;

                    case 1:
                        spinner.setSelection(UserPreferences.getTabTwoSpinnerPosition(getApplicationContext()));
                        break;

                    case 2:
                        spinner.setSelection(UserPreferences.getTabThreeSpinnerPosition(getApplicationContext()));
                        break;
                }

                manuallySelected = true;

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(manuallySelected)
                {
                    manuallySelected = false;
                    return;
                }

                EffectsManager.updateValues = false;

                String item = spinner.getSelectedItem().toString();

                Bluetooth.write(item + " was selected\n");

                switch(tabSelected)
                {
                    case 0:
                        UserPreferences.setTabOneEffect(getApplicationContext(), item, spinner.getSelectedItemPosition());
                        break;

                    case 1:
                        UserPreferences.setTabTwoEffect(getApplicationContext(), item, spinner.getSelectedItemPosition());
                        break;

                    case 2:
                        UserPreferences.setTabThreeEffect(getApplicationContext(), item, spinner.getSelectedItemPosition());
                        break;
                }

                tabManuallySelected = true;

                EffectsManager.currentTabOne = null;
                EffectsManager.currentTabTwo = null;
                EffectsManager.currentTabThree = null;

                effectsPageAdapter.updateTab(tabSelected);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(!Bluetooth.isConnected())
        {
            Bluetooth.connectToDevice();
        }


        if(EffectsManager.currentEffect != null)
        {
            effectName.setText(EffectsManager.currentEffect.getName());
        }
        else
        {
            effectName.setText("");
        }

    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.effects_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {

            case R.id.remove_effect_button:
                if(effectsPageAdapter.removeTab(tabSelected))
                {
                    switch (effectsPageAdapter.getCount())
                    {
                        case 1:
                            EffectsManager.currentTabTwo = null;
                            break;

                        case 2:
                            EffectsManager.currentTabThree = null;
                            break;

                        default:
                            break;
                    }
                }
                return true;

            case R.id.add_effect_button:
                effectsPageAdapter.addTab();
                return true;

            case R.id.save_effect_button:

                if(EffectsManager.currentEffect == null)
                {
                    EffectsManager.saveNewEffect(this);
//                    List<BaseEffect.EffectDuple> listOne = new ArrayList<>();
//                    listOne.add(new BaseEffect.EffectDuple(R.id.delay_delay_seekbar, 0, false, false, false));
//                    EffectsManager.saveNewEffect(this, new BaseEffect("Delay", 2, this, ), null, null);
                }
                else
                {
                    EffectsManager.saveOverEffect(this);
                }
                return true;

            case R.id.open_effect_button:
                EffectsManager.openEffect(this, effectsPageAdapter);
                return true;

            case R.id.create_new_effect_button:
                EffectsManager.currentEffect = null;
                EffectsManager.currentTabTwo = null;
                EffectsManager.currentTabThree = null;
                effectsPageAdapter.removeTab(tabSelected);
                effectsPageAdapter.removeTab(tabSelected);
                effectName.setText("");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
