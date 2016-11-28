package com.noahpena.multi_effect_guitar_pedal.Activities;

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

import com.noahpena.multi_effect_guitar_pedal.R;


/**
 * Created by noah-pena on 11/6/16.
 */

public class EffectsActivity extends AppCompatActivity
{

    Toolbar toolbar;
    Spinner spinner;
    ViewPager viewPager;

    int tabSelected = 0;

    EffectsPageAdapter effectsPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effects_layout);

        Bluetooth.init(this);

        spinner = (Spinner)findViewById(R.id.effectsSpinner);

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

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
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

        //getSupportFragmentManager().beginTransaction().add(R.id.effects_frame, fragment).commit();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
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

                effectsPageAdapter.notifyDataSetChanged();
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
        Bluetooth.connectToDevice();
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.effects_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
