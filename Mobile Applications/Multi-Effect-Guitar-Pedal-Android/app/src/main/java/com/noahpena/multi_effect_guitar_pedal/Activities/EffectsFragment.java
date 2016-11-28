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

        View view = inflater.inflate(layout, container, false);

        switch(result)
        {
            case "Chorus":
                view = setupChorusView(view);
                break;

            case "Delay":
                view = setupDelayView(view);
                break;

            case "Reverb":
                view = setupReverbView(view);
                break;

            default:
                view = setupChorusView(view);
                break;
        }

        // Inflate the layout for this fragment
        return view;
    }

    public View setupChorusView(View view)
    {

        return view;
    }

    public View setupDelayView(View view)
    {
        return view;
    }

    public View setupReverbView(View view)
    {
        return view;
    }

}
