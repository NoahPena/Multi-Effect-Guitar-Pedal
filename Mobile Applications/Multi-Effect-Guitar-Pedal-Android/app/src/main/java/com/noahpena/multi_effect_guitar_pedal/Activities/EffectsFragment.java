package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noahpena.multi_effect_guitar_pedal.R;

/**
 * Created by noah-pena on 11/6/16.
 */

public class EffectsFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        int layout = 0;

        Bundle b = this.getArguments();

        String result = b.getString("Effect", "ERROR");

        Log.d("DEBUG", result);

        switch(result)
        {
            case "Chorus":
                layout = R.layout.chorus_effect_layout;
                break;

            case "Delay":
                layout = R.layout.delay_effect_layout;
                break;

            case "Reverb":
                layout = R.layout.reverb_effect_layout;
                break;

            default:
                layout = R.layout.chorus_effect_layout;
                break;
        }
        // Inflate the layout for this fragment
        return inflater.inflate(layout, container, false);
    }
}
