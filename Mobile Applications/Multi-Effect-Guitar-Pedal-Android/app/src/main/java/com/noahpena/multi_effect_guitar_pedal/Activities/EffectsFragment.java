package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noah-pena on 11/6/16.
 */

public class EffectsFragment extends Fragment
{

    public int tabNumber = -1;

    public RelativeLayout root;

    public String result;

    public int spinnerValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        int layout = 0;

        Bundle b = this.getArguments();

        result = b.getString("Effect", "ERROR");
        tabNumber = b.getInt("TabNumber", -1);
        spinnerValue = b.getInt("SpinnerValue", 0);

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

        switch (tabNumber)
        {
            case 0:
                //if(EffectsManager.currentTabOne == null)
                //{
                    EffectsManager.currentTabOne = new BaseEffect(result, spinnerValue, root);
                //}
                //else
                //{
                //    updateValues();
                //}
                break;

            case 1:
                //if(EffectsManager.currentTabTwo == null)
                //{
                    EffectsManager.currentTabTwo = new BaseEffect(result, spinnerValue, root);
                //}
                //else
                //{
                 //   updateValues();
                //}
                break;

            case 2:
                //if(EffectsManager.currentTabThree == null)
                //{
                    EffectsManager.currentTabThree = new BaseEffect(result, spinnerValue, root);
                //}
                //else
                //{
                //    updateValues();
                //}
                break;
        }




        // Inflate the layout for this fragment
        return view;
    }

    public void updateValues()
    {
        ArrayList<SeekBar> seekBars = new ArrayList<>();
        ArrayList<Switch> switches = new ArrayList<>();

        final int childCount = root.getChildCount();

        for(int i = 0; i < childCount; i++)
        {
            View v = root.getChildAt(i);

            if(v instanceof SeekBar)
            {
                SeekBar seekbar = (SeekBar)v;

                seekBars.add(seekbar);
            }
            else if(v instanceof Switch)
            {
                Switch sweetch = (Switch)v;

                switches.add(sweetch);
            }
        }

        List<BaseEffect.EffectDuple> values;

        switch(tabNumber)
        {
            case 0:
                values = EffectsManager.currentTabOne.getParameters();
                break;

            case 1:
                values = EffectsManager.currentTabTwo.getParameters();
                break;

            case 2:
                values = EffectsManager.currentTabThree.getParameters();
                break;

            default:
                values = EffectsManager.currentTabOne.getParameters();
                break;
        }

        for(int i = 0; i < values.size(); i++)
        {
            BaseEffect.EffectDuple temp = values.get(i);

            if(temp.sliderValue == -1)
            {
                for(int j = 0; j < switches.size(); j++)
                {
                    if(temp.elementID == switches.get(j).getId())
                    {
                        switches.get(j).setChecked(temp.switchValue);
                        break;
                    }
                }
            }
            else
            {
                for(int j = 0; j < seekBars.size(); j++)
                {
                    if(temp.elementID == seekBars.get(j).getId())
                    {
                        seekBars.get(j).setProgress(temp.sliderValue);
                    }
                }
            }
        }


    }

    public void sendValues()
    {

    }

    public View setupChorusView(View view)
    {

        root = (RelativeLayout)view.findViewById(R.id.chorus_root_layout);


        return view;
    }

    public View setupDelayView(View view)
    {

        root = (RelativeLayout)view.findViewById(R.id.delay_root_layout);

        TextView delayTextView = (TextView)view.findViewById(R.id.delay_delay_value);

        SeekBar delaySeekbar = (SeekBar)view.findViewById(R.id.delay_delay_seekbar);

        delaySeekbar.setMax(300);

        delaySeekbar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayTextView));


        return view;
    }

    public View setupReverbView(View view)
    {

        root = (RelativeLayout)view.findViewById(R.id.reverb_root_layout);

        return view;
    }

    class SeekbarEffectListener implements SeekBar.OnSeekBarChangeListener
    {

        TextView value;

        public SeekbarEffectListener(TextView textView)
        {
            this.value = textView;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b)
        {
            value.setText(Double.toString((double)i/100));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            sendValues();
        }
    }

}
