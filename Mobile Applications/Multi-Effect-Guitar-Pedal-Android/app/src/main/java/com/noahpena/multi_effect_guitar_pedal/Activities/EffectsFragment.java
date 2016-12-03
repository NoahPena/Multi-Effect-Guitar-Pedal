package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noah-pena on 11/6/16.
 */

public class EffectsFragment extends Fragment
{

    public int tabNumber = -1;

    public RelativeLayout root;

    public View fragmentView;

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

            case "Low Pass":
                layout = R.layout.lowpass_effect_layout;
                break;

            case "High Pass":
                layout = R.layout.highpass_effect_layout;
                break;

            case "Overdrive":
                layout = R.layout.overdrive_effect_layout;
                break;

            case "Tremolo":
                layout = R.layout.tremolo_effect_layout;
                break;

            case "Phaser":
                layout = R.layout.phaser_effect_layout;
                break;

            case "Echo":
                layout = R.layout.echo_effect_layout;
                break;

            case "Flanger":
                layout = R.layout.flanger_effect_layout;
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

            case "Low Pass":
                view = setupLowpassView(view);
                break;

            case "High Pass":
                view = setupHighpassView(view);
                break;

            case "Overdrive":
                view = setupOverdriveView(view);
                break;

            case "Tremolo":
                view = setupTremoloView(view);
                break;

            case "Phaser":
                view = setupPhaserView(view);
                break;

            case "Echo":
                view = setupEchoView(view);
                break;

            case "Flanger":
                view = setupFlangerView(view);
                break;

            default:
                view = setupChorusView(view);
                break;
        }

        fragmentView = view;

        switch (tabNumber)
        {
            case 0:
                if(!EffectsManager.updateTabOne)
                {
                    EffectsManager.currentTabOne = new BaseEffect(result, spinnerValue, this);
                }
                else
                {
                    updateValues();

                    EffectsManager.updateTabOne = false;
                }
                break;

            case 1:
                if(!EffectsManager.updateTabTwo)
                {
                    EffectsManager.currentTabTwo = new BaseEffect(result, spinnerValue, this);
                }
                else
                {
                    updateValues();
                    EffectsManager.updateTabTwo = false;
                }
                break;

            case 2:
                if(!EffectsManager.updateTabThree)
                {
                    EffectsManager.currentTabThree = new BaseEffect(result, spinnerValue, this);
                }
                else
                {
                    updateValues();
                    EffectsManager.updateTabThree = false;
                }
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
                Log.d("DEBUG", "1Command: " + EffectsManager.currentTabOne.getCommand());
                break;

            case 1:
                values = EffectsManager.currentTabTwo.getParameters();
                Log.d("DEBUG", "2Command: " + EffectsManager.currentTabTwo.getCommand());
                break;

            case 2:
                values = EffectsManager.currentTabThree.getParameters();
                Log.d("DEBUG", "3Command: " + EffectsManager.currentTabThree.getCommand());
                break;

            default:
                values = EffectsManager.currentTabOne.getParameters();
                Log.d("DEBUG", "1Command: " + EffectsManager.currentTabOne.getCommand());
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
        switch(tabNumber)
        {
            case 0:
                EffectsManager.currentTabOne = new BaseEffect(result, spinnerValue, this);
                break;

            case 1:
                EffectsManager.currentTabTwo = new BaseEffect(result, spinnerValue, this);
                break;

            case 2:
                EffectsManager.currentTabThree = new BaseEffect(result, spinnerValue, this);
                break;
        }

        //Send Bluetooth Packet of UserEffect
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
        delayTextView.setText("0s");

        SeekBar delaySeekbar = (SeekBar)view.findViewById(R.id.delay_delay_seekbar);
        delaySeekbar.setMax(300);
        delaySeekbar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayTextView, "s"));


        return view;
    }

    public View setupReverbView(View view)
    {

        root = (RelativeLayout)view.findViewById(R.id.reverb_root_layout);

        return view;
    }

    public View setupLowpassView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.lowpass_root_layout);

        TextView lowpassTextView = (TextView)view.findViewById(R.id.lowpass_frequency_value);
        lowpassTextView.setText("1000Hz");

        SeekBar lowpassSeekBar = (SeekBar)view.findViewById(R.id.lowpass_frequency_seekbar);
        lowpassSeekBar.setProgress(0);
        lowpassSeekBar.setMax(8000 * 100);
        lowpassSeekBar.setProgress(1000 * 100);
        lowpassSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(lowpassTextView, "Hz"));

        return view;
    }

    public View setupHighpassView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.highpass_root_layout);

        TextView highpassTextView = (TextView)view.findViewById(R.id.highpass_frequency_value);
        highpassTextView.setText("3000Hz");

        SeekBar highpassSeekBar = (SeekBar)view.findViewById(R.id.highpass_frequency_seekbar);
        highpassSeekBar.setProgress(0);
        highpassSeekBar.setMax(15000 * 100);
        highpassSeekBar.setProgress(3000 * 100);
        highpassSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(highpassTextView, "Hz"));

        return view;
    }

    public View setupOverdriveView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.overdrive_root_layout);

        final TextView overdriveGainTextView = (TextView)view.findViewById(R.id.overdrive_gain_value);
        overdriveGainTextView.setText("20dB");

        final TextView overdriveColorTextView = (TextView)view.findViewById(R.id.overdrive_color_value);
        overdriveColorTextView.setText("20HD");

        final SeekBar overdriveGainSeekBar = (SeekBar)view.findViewById(R.id.overdrive_gain_seekbar);
        overdriveGainSeekBar.setProgress(0);
        overdriveGainSeekBar.setMax(40 * 100);
        overdriveGainSeekBar.setProgress(20 * 100);
        overdriveGainSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(overdriveGainTextView, "dB"));

        final SeekBar overdriveColorSeekBar = (SeekBar)view.findViewById(R.id.overdrive_color_seekbar);
        overdriveColorSeekBar.setProgress(0);
        overdriveColorSeekBar.setMax(40 * 100);
        overdriveColorSeekBar.setProgress(20 * 100);
        overdriveColorSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(overdriveColorTextView, "HD"));

        Button overdriveResetButton = (Button)view.findViewById(R.id.overdrive_default_button);
        overdriveResetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                overdriveGainTextView.setText("20dB");
                overdriveColorTextView.setText("20HD");

                overdriveGainSeekBar.setProgress(0);
                overdriveGainSeekBar.setProgress(20 * 100);

                overdriveColorSeekBar.setProgress(0);
                overdriveColorSeekBar.setProgress(20 * 100);
            }
        });


        return view;
    }

    public View setupTremoloView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.tremolo_root_layout);

        return view;
    }

    public View setupPhaserView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.phaser_root_layout);

        return view;
    }

    public View setupEchoView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.echo_root_layout);

        return view;
    }

    public View setupFlangerView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.flanger_root_layout);

        return view;
    }





    class SeekbarEffectListener implements SeekBar.OnSeekBarChangeListener
    {

        TextView value;
        String unit;

        public SeekbarEffectListener(TextView textView, String units)
        {
            this.value = textView;
            this.unit = units;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b)
        {
            value.setText(Double.toString((double)i/100) + this.unit);
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
