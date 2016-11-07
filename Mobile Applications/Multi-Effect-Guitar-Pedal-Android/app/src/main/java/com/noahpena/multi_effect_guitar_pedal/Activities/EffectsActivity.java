package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effects_layout);

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
                finish();
            }
        });

        Log.d("DEBUG", spinner.getSelectedItem().toString());

        Bundle b = new Bundle();
        b.putString("Effect", spinner.getSelectedItem().toString());


        EffectsFragment fragment = new EffectsFragment();
        fragment.setArguments(b);


        getSupportFragmentManager().beginTransaction().add(R.id.effects_frame, fragment).commit();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String item = spinner.getSelectedItem().toString();

                Bundle b = new Bundle();
                b.putString("Effect", item);

                EffectsFragment fragment = new EffectsFragment();
                fragment.setArguments(b);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.effects_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
