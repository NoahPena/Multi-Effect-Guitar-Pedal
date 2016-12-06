package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

import java.io.Serializable;
import java.nio.channels.SelectionKey;
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
                        seekBars.get(j).setProgress(0);
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
        Bluetooth.writeString("padsp sox --buffer 1024 -d -d " + new UserEffect("temp" , EffectsManager.currentTabOne, EffectsManager.currentTabTwo, EffectsManager.currentTabThree).getCommand());

    }

    public View setupChorusView(View view)
    {

        root = (RelativeLayout)view.findViewById(R.id.chorus_root_layout);

        final TextView gainInTextView = (TextView)view.findViewById(R.id.chorus_gain_in_value);
        gainInTextView.setText(".5dB");

        TextView gainOutTextView = (TextView)view.findViewById(R.id.chorus_gain_out_value);
        gainOutTextView.setText(".5dB");

        final TextView delayOneTextView = (TextView)view.findViewById(R.id.chorus_delay_one_value);
        delayOneTextView.setText("60ms");

        final TextView decayOneTextView = (TextView)view.findViewById(R.id.chorus_decay_one_value);
        decayOneTextView.setText(".4ms");

        final TextView speedOneTextView = (TextView)view.findViewById(R.id.chorus_speed_one_value);
        speedOneTextView.setText(".25Hz");

        final TextView depthOneTextView = (TextView)view.findViewById(R.id.chorus_depth_one_value);
        depthOneTextView.setText("2ms");

        TextView delayTwoTextView = (TextView)view.findViewById(R.id.chorus_delay_two_value);
        delayTwoTextView.setText("60ms");

        TextView decayTwoTextView = (TextView)view.findViewById(R.id.chorus_decay_two_value);
        decayTwoTextView.setText(".4ms");

        TextView speedTwoTextView = (TextView)view.findViewById(R.id.chorus_speed_two_value);
        speedTwoTextView.setText(".25Hz");

        TextView depthTwoTextView = (TextView)view.findViewById(R.id.chorus_depth_two_value);
        depthTwoTextView.setText("2ms");

        TextView delayThreeTextView = (TextView)view.findViewById(R.id.chorus_delay_three_value);
        delayThreeTextView.setText("60ms");

        TextView decayThreeTextView = (TextView)view.findViewById(R.id.chorus_decay_three_value);
        decayThreeTextView.setText(".4ms");

        TextView speedThreeTextView = (TextView)view.findViewById(R.id.chorus_speed_three_value);
        speedThreeTextView.setText(".25Hz");

        TextView depthThreeTextView = (TextView)view.findViewById(R.id.chorus_depth_three_value);
        depthThreeTextView.setText("2ms");


        final SeekBar gainInSeekBar = (SeekBar)view.findViewById(R.id.chorus_gain_in_seekbar);
        gainInSeekBar.setProgress(0);
        gainInSeekBar.setMax(1 * 100);
        gainInSeekBar.setProgress((int).5 * 100);
        gainInSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(gainInTextView, "dB"));

        final SeekBar gainOutSeekBar = (SeekBar)view.findViewById(R.id.chorus_gain_out_seekbar);
        gainOutSeekBar.setProgress(0);
        gainOutSeekBar.setMax(1 * 100);
        gainOutSeekBar.setProgress((int).5 * 100);
        gainOutSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(gainOutTextView, "dB"));

        final SeekBar delayOneSeekBar = (SeekBar)view.findViewById(R.id.chorus_delay_one_seekbar);
        delayOneSeekBar.setProgress(0);
        delayOneSeekBar.setMax(100 * 100);
        delayOneSeekBar.setProgress(60 * 100);
        delayOneSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayOneTextView, "ms"));
        delayOneSeekBar.setEnabled(true);

        final SeekBar decayOneSeekBar = (SeekBar)view.findViewById(R.id.chorus_decay_one_seekbar);
        decayOneSeekBar.setProgress(0);
        decayOneSeekBar.setMax(1 * 100);
        decayOneSeekBar.setProgress((int).4 * 100);
        decayOneSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(decayOneTextView, "ms"));
        decayOneSeekBar.setEnabled(true);

        final SeekBar speedOneSeekBar = (SeekBar)view.findViewById(R.id.chorus_speed_one_seekbar);
        speedOneSeekBar.setProgress(0);
        speedOneSeekBar.setMax(5 * 100);
        speedOneSeekBar.setProgress((int).25 * 100);
        speedOneSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(speedOneTextView, "Hz"));
        speedOneSeekBar.setEnabled(true);

        final SeekBar depthOneSeekBar = (SeekBar)view.findViewById(R.id.chorus_depth_one_seekbar);
        depthOneSeekBar.setProgress(0);
        depthOneSeekBar.setMax(10 * 100);
        depthOneSeekBar.setProgress((int)2 * 100);
        depthOneSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(depthOneTextView, "ms"));
        depthOneSeekBar.setEnabled(true);

        //

        final SeekBar delayTwoSeekBar = (SeekBar)view.findViewById(R.id.chorus_delay_two_seekbar);
        delayTwoSeekBar.setProgress(0);
        delayTwoSeekBar.setMax(100 * 100);
        delayTwoSeekBar.setProgress(60 * 100);
        delayTwoSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayOneTextView, "ms"));
        delayTwoSeekBar.setEnabled(false);

        final SeekBar decayTwoSeekBar = (SeekBar)view.findViewById(R.id.chorus_decay_two_seekbar);
        decayTwoSeekBar.setProgress(0);
        decayTwoSeekBar.setMax(1 * 100);
        decayTwoSeekBar.setProgress((int).4 * 100);
        decayTwoSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(decayOneTextView, "ms"));
        decayTwoSeekBar.setEnabled(false);

        final SeekBar speedTwoSeekBar = (SeekBar)view.findViewById(R.id.chorus_speed_two_seekbar);
        speedTwoSeekBar.setProgress(0);
        speedTwoSeekBar.setMax(5 * 100);
        speedTwoSeekBar.setProgress((int).25 * 100);
        speedTwoSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(speedOneTextView, "Hz"));
        speedTwoSeekBar.setEnabled(false);

        final SeekBar depthTwoSeekBar = (SeekBar)view.findViewById(R.id.chorus_depth_two_seekbar);
        depthTwoSeekBar.setProgress(0);
        depthTwoSeekBar.setMax(10 * 100);
        depthTwoSeekBar.setProgress((int)2 * 100);
        depthTwoSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(depthOneTextView, "ms"));
        depthTwoSeekBar.setEnabled(false);

        //

        final SeekBar delayThreeSeekBar = (SeekBar)view.findViewById(R.id.chorus_delay_three_seekbar);
        delayThreeSeekBar.setProgress(0);
        delayThreeSeekBar.setMax(100 * 100);
        delayThreeSeekBar.setProgress(60 * 100);
        delayThreeSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayOneTextView, "ms"));
        delayThreeSeekBar.setEnabled(false);

        final SeekBar decayThreeSeekBar = (SeekBar)view.findViewById(R.id.chorus_decay_three_seekbar);
        decayThreeSeekBar.setProgress(0);
        decayThreeSeekBar.setMax(1 * 100);
        decayThreeSeekBar.setProgress((int).4 * 100);
        decayThreeSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(decayOneTextView, "ms"));
        decayThreeSeekBar.setEnabled(false);

        final SeekBar speedThreeSeekBar = (SeekBar)view.findViewById(R.id.chorus_speed_three_seekbar);
        speedThreeSeekBar.setProgress(0);
        speedThreeSeekBar.setMax(5 * 100);
        speedThreeSeekBar.setProgress((int).25 * 100);
        speedThreeSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(speedOneTextView, "Hz"));
        speedThreeSeekBar.setEnabled(false);

        final SeekBar depthThreeSeekBar = (SeekBar)view.findViewById(R.id.chorus_depth_three_seekbar);
        depthThreeSeekBar.setProgress(0);
        depthThreeSeekBar.setMax(10 * 100);
        depthThreeSeekBar.setProgress((int)2 * 100);
        depthThreeSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(depthOneTextView, "ms"));
        depthThreeSeekBar.setEnabled(false);


        final Switch modulationOneSwitch = (Switch)view.findViewById(R.id.chorus_modulation_one_switch);
        modulationOneSwitch.setChecked(false);
        modulationOneSwitch.setEnabled(true);

        final Switch modulationTwoSwitch = (Switch)view.findViewById(R.id.chorus_modulation_two_switch);
        modulationTwoSwitch.setChecked(false);
        modulationTwoSwitch.setEnabled(false);

        final Switch modulationThreeSwitch = (Switch)view.findViewById(R.id.chorus_modulation_three_switch);
        modulationThreeSwitch.setChecked(false);
        modulationThreeSwitch.setEnabled(false);
        

        final Switch mainDelayOneSwitch = (Switch)view.findViewById(R.id.chorus_delay_group_one_switch);
        mainDelayOneSwitch.setChecked(true);
        mainDelayOneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                delayOneSeekBar.setEnabled(b);
                decayOneSeekBar.setEnabled(b);
                speedOneSeekBar.setEnabled(b);
                depthOneSeekBar.setEnabled(b);
                modulationOneSwitch.setEnabled(b);
            }
        });

        final Switch mainDelayTwoSwitch = (Switch)view.findViewById(R.id.chorus_delay_group_two_switch);
        mainDelayTwoSwitch.setChecked(false);
        mainDelayTwoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                delayTwoSeekBar.setEnabled(b);
                decayTwoSeekBar.setEnabled(b);
                speedTwoSeekBar.setEnabled(b);
                depthTwoSeekBar.setEnabled(b);
                modulationTwoSwitch.setEnabled(b);
            }
        });

        final Switch mainDelayThreeSwitch = (Switch)view.findViewById(R.id.chorus_delay_group_three_switch);
        mainDelayThreeSwitch.setChecked(false);
        mainDelayThreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                delayThreeSeekBar.setEnabled(b);
                decayThreeSeekBar.setEnabled(b);
                speedThreeSeekBar.setEnabled(b);
                depthThreeSeekBar.setEnabled(b);
                modulationThreeSwitch.setEnabled(b);
            }
        });


        Button resetButton = (Button)view.findViewById(R.id.chorus_defaults_button);
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainDelayOneSwitch.setChecked(true);
                mainDelayTwoSwitch.setChecked(false);
                mainDelayThreeSwitch.setChecked(false);

                gainInSeekBar.setProgress(0);
                gainInSeekBar.setProgress((int).5 * 100);

                gainOutSeekBar.setProgress(0);
                gainOutSeekBar.setProgress((int).5 * 100);

                delayOneSeekBar.setEnabled(true);
                delayOneSeekBar.setProgress(0);
                delayOneSeekBar.setProgress(60 * 100);

                decayOneSeekBar.setEnabled(true);
                decayOneSeekBar.setProgress(0);
                decayOneSeekBar.setProgress((int).4 * 100);

                speedOneSeekBar.setEnabled(true);
                speedOneSeekBar.setProgress(0);
                speedOneSeekBar.setProgress((int).25 * 100);

                depthOneSeekBar.setEnabled(true);
                depthOneSeekBar.setProgress(0);
                depthOneSeekBar.setProgress((int)2 * 100);

                delayTwoSeekBar.setEnabled(true);
                delayTwoSeekBar.setProgress(0);
                delayTwoSeekBar.setProgress(60 * 100);

                decayTwoSeekBar.setEnabled(true);
                decayTwoSeekBar.setProgress(0);
                decayTwoSeekBar.setProgress((int).4 * 100);

                speedTwoSeekBar.setEnabled(true);
                speedTwoSeekBar.setProgress(0);
                speedTwoSeekBar.setProgress((int).25 * 100);

                depthTwoSeekBar.setEnabled(true);
                depthTwoSeekBar.setProgress(0);
                depthTwoSeekBar.setProgress((int)2 * 100);

                delayThreeSeekBar.setEnabled(true);
                delayThreeSeekBar.setProgress(0);
                delayThreeSeekBar.setProgress(60 * 100);

                decayThreeSeekBar.setEnabled(true);
                decayThreeSeekBar.setProgress(0);
                decayThreeSeekBar.setProgress((int).4 * 100);

                speedThreeSeekBar.setEnabled(true);
                speedThreeSeekBar.setProgress(0);
                speedThreeSeekBar.setProgress((int).25 * 100);

                depthThreeSeekBar.setEnabled(true);
                depthThreeSeekBar.setProgress(0);
                depthThreeSeekBar.setProgress((int)2 * 100);
            }
        });


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

        TextView reverberanceTextView = (TextView)view.findViewById(R.id.reverb_reverberance_value);
        reverberanceTextView.setText("50%");

        final TextView hfTextView = (TextView)view.findViewById(R.id.reverb_hf_damping_value);
        hfTextView.setText("50%");

        final TextView roomTextView = (TextView)view.findViewById(R.id.reverb_room_scale_value);
        roomTextView.setText("100%");

        final TextView stereoTextView = (TextView)view.findViewById(R.id.reverb_stereo_depth_value);
        stereoTextView.setText("100%");

        final TextView delayTextView = (TextView)view.findViewById(R.id.reverb_pre_delay_value);
        delayTextView.setText("0ms");

        TextView wetTextView = (TextView)view.findViewById(R.id.reverb_wet_gain_value);
        wetTextView.setText("0dB");

        final SeekBar reverbSeekBar = (SeekBar)view.findViewById(R.id.reverb_reverberance_seekbar);
        reverbSeekBar.setProgress(0);
        reverbSeekBar.setMax(100 * 100);
        reverbSeekBar.setProgress(50 * 100);
        reverbSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(reverberanceTextView, "%"));

        final SeekBar hfSeekBar = (SeekBar)view.findViewById(R.id.reverb_hf_damping_seekbar);
        hfSeekBar.setProgress(0);
        hfSeekBar.setMax(100 * 100);
        hfSeekBar.setProgress(50 * 100);
        hfSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(hfTextView, "%"));

        final SeekBar roomSeekBar = (SeekBar)view.findViewById(R.id.reverb_room_scale_seekbar);
        roomSeekBar.setProgress(0);
        roomSeekBar.setMax(100 * 100);
        roomSeekBar.setProgress(100 * 100);
        roomSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(roomTextView, "%"));

        final SeekBar stereoSeekBar = (SeekBar)view.findViewById(R.id.reverb_stereo_depth_seekbar);
        stereoSeekBar.setProgress(0);
        stereoSeekBar.setMax(100 * 100);
        stereoSeekBar.setProgress(100 * 100);
        stereoSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(stereoTextView, "%"));

        final SeekBar delaySeekBar = (SeekBar)view.findViewById(R.id.reverb_pre_delay_seekbar);
        delaySeekBar.setProgress(0);
        delaySeekBar.setMax(500 * 100);
        delaySeekBar.setProgress(0);
        delaySeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayTextView, "ms"));

        final SeekBar wetSeekBar = (SeekBar)view.findViewById(R.id.reverb_wet_gain_seekbar);
        wetSeekBar.setProgress(0);
        wetSeekBar.setMax(10 * 100);
        wetSeekBar.setProgress(0);
        wetSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(wetTextView, "dB"));

        Button resetButton = (Button)view.findViewById(R.id.reverb_reset_button);
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                reverbSeekBar.setProgress(0);
                reverbSeekBar.setProgress(50 * 100);

                hfSeekBar.setProgress(0);
                hfSeekBar.setProgress(50 * 100);

                roomSeekBar.setProgress(0);
                roomSeekBar.setProgress(100);

                stereoSeekBar.setProgress(0);
                stereoSeekBar.setProgress(100 * 100);

                delaySeekBar.setProgress(0);
                delaySeekBar.setProgress(0);

                wetSeekBar.setProgress(0);
                wetSeekBar.setProgress(0);
            }
        });

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

        final TextView speedTextView = (TextView)view.findViewById(R.id.tremolo_speed_value);
        speedTextView.setText("1000Hz");

        final TextView depthTextView = (TextView)view.findViewById(R.id.tremolo_depth_value);
        depthTextView.setText("40%");

        final SeekBar speedSeekBar = (SeekBar)view.findViewById(R.id.tremolo_speed_seekbar);
        speedSeekBar.setProgress(0);
        speedSeekBar.setMax(8000 * 100);
        speedSeekBar.setProgress(1000 * 100);
        speedSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(speedTextView, "Hz"));

        final SeekBar depthSeekBar = (SeekBar)view.findViewById(R.id.tremolo_depth_seekbar);
        depthSeekBar.setProgress(0);
        depthSeekBar.setMax(100 * 100);
        depthSeekBar.setProgress(40 * 100);
        depthSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(depthTextView, "%"));

        Button resetButton = (Button)view.findViewById(R.id.tremolo_default_button);
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                speedTextView.setText("1000Hz");
                depthTextView.setText("40%");

                speedSeekBar.setProgress(0);
                speedSeekBar.setProgress(1000 * 100);

                depthSeekBar.setProgress(0);
                depthSeekBar.setProgress(40 * 100);
            }
        });

        return view;
    }

    public View setupPhaserView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.phaser_root_layout);

        final TextView gainInTextView = (TextView)view.findViewById(R.id.phaser_gain_in_value);
        gainInTextView.setText("20dB");

        final TextView gainOutTextView = (TextView)view.findViewById(R.id.phaser_gain_out_value);
        gainOutTextView.setText("20dB");

        final TextView delayTextView = (TextView)view.findViewById(R.id.phaser_delay_one_value);
        delayTextView.setText("1000ms");

        final TextView decayTextView = (TextView)view.findViewById(R.id.phaser_decay_one_value);
        decayTextView.setText(".25s");

        final TextView speedTextView = (TextView)view.findViewById(R.id.phaser_speed_one_value);
        speedTextView.setText("1000Hz");

        final SeekBar gainInSeekBar = (SeekBar)view.findViewById(R.id.phaser_gain_in_seekbar);
        gainInSeekBar.setProgress(0);
        gainInSeekBar.setMax(40 * 100);
        gainInSeekBar.setProgress(20 * 100);
        gainInSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(gainInTextView, "dB"));

        final SeekBar gainOutSeekBar = (SeekBar)view.findViewById(R.id.phaser_gain_out_seekbar);
        gainOutSeekBar.setProgress(0);
        gainOutSeekBar.setMax(40 * 100);
        gainOutSeekBar.setProgress(20 * 100);
        gainOutSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(gainOutTextView, "dB"));

        final SeekBar delaySeekBar = (SeekBar)view.findViewById(R.id.phaser_delay_one_seekbar);
        delaySeekBar.setProgress(0);
        delaySeekBar.setMax(3000 * 100);
        delaySeekBar.setProgress(1000 * 100);
        delaySeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayTextView, "ms"));

        final SeekBar decaySeekBar = (SeekBar)view.findViewById(R.id.phaser_decay_one_seekbar);
        decaySeekBar.setProgress(0);
        decaySeekBar.setMax((int).5 * 100);
        decaySeekBar.setProgress((int).25 * 100);
        decaySeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(decayTextView, "s"));

        final SeekBar speedSeekBar = (SeekBar)view.findViewById(R.id.phaser_speed_one_seekbar);
        speedSeekBar.setProgress(0);
        speedSeekBar.setMax(8000 * 100);
        speedSeekBar.setProgress(1000 * 100);
        speedSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(speedTextView, "Hz"));

        final Switch modulationSwitch = (Switch)view.findViewById(R.id.phaser_modulation_one_switch);

        Button resetButton = (Button)view.findViewById(R.id.phaser_defaults_button);
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gainInTextView.setText("20dB");
                speedTextView.setText("1000Hz");
                decayTextView.setText(".25s");
                delayTextView.setText("1000ms");
                gainOutTextView.setText("20dB");

                gainInSeekBar.setProgress(0);
                gainInSeekBar.setProgress(20 * 100);

                gainOutSeekBar.setProgress(0);
                gainOutSeekBar.setProgress(20 * 100);

                delaySeekBar.setProgress(0);
                delaySeekBar.setProgress(1000 * 100);

                decaySeekBar.setProgress(0);
                decaySeekBar.setProgress((int).25 * 100);

                decaySeekBar.setProgress(0);
                speedSeekBar.setProgress(1000 * 100);

                modulationSwitch.setChecked(false);
            }
        });

        return view;
    }

    public View setupEchoView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.echo_root_layout);


        final TextView gainInTextView = (TextView)view.findViewById(R.id.echo_gain_in_value);
        gainInTextView.setText(".5dB");

        TextView gainOutTextView = (TextView)view.findViewById(R.id.echo_gain_out_value);
        gainOutTextView.setText(".5dB");

        final TextView delayOneTextView = (TextView)view.findViewById(R.id.echo_delay_one_value);
        delayOneTextView.setText("60ms");

        final TextView decayOneTextView = (TextView)view.findViewById(R.id.echo_decay_one_value);
        decayOneTextView.setText(".4ms");

        TextView delayTwoTextView = (TextView)view.findViewById(R.id.echo_delay_two_value);
        delayTwoTextView.setText("60ms");

        TextView decayTwoTextView = (TextView)view.findViewById(R.id.echo_decay_two_value);
        decayTwoTextView.setText(".4ms");

        TextView delayThreeTextView = (TextView)view.findViewById(R.id.echo_delay_three_value);
        delayThreeTextView.setText("60ms");

        TextView decayThreeTextView = (TextView)view.findViewById(R.id.echo_decay_three_value);
        decayThreeTextView.setText(".4ms");


        final SeekBar gainInSeekBar = (SeekBar)view.findViewById(R.id.echo_gain_in_seekbar);
        gainInSeekBar.setProgress(0);
        gainInSeekBar.setMax(1 * 100);
        gainInSeekBar.setProgress((int).5 * 100);
        gainInSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(gainInTextView, "dB"));

        final SeekBar gainOutSeekBar = (SeekBar)view.findViewById(R.id.echo_gain_out_seekbar);
        gainOutSeekBar.setProgress(0);
        gainOutSeekBar.setMax(1 * 100);
        gainOutSeekBar.setProgress((int).5 * 100);
        gainOutSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(gainOutTextView, "dB"));

        final SeekBar delayOneSeekBar = (SeekBar)view.findViewById(R.id.echo_delay_one_seekbar);
        delayOneSeekBar.setProgress(0);
        delayOneSeekBar.setMax(100 * 100);
        delayOneSeekBar.setProgress(60 * 100);
        delayOneSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayOneTextView, "ms"));
        delayOneSeekBar.setEnabled(true);

        final SeekBar decayOneSeekBar = (SeekBar)view.findViewById(R.id.echo_decay_one_seekbar);
        decayOneSeekBar.setProgress(0);
        decayOneSeekBar.setMax(1 * 100);
        decayOneSeekBar.setProgress((int).4 * 100);
        decayOneSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(decayOneTextView, "ms"));
        decayOneSeekBar.setEnabled(true);

        //

        final SeekBar delayTwoSeekBar = (SeekBar)view.findViewById(R.id.echo_delay_two_seekbar);
        delayTwoSeekBar.setProgress(0);
        delayTwoSeekBar.setMax(100 * 100);
        delayTwoSeekBar.setProgress(60 * 100);
        delayTwoSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayOneTextView, "ms"));
        delayTwoSeekBar.setEnabled(false);

        final SeekBar decayTwoSeekBar = (SeekBar)view.findViewById(R.id.echo_decay_two_seekbar);
        decayTwoSeekBar.setProgress(0);
        decayTwoSeekBar.setMax(1 * 100);
        decayTwoSeekBar.setProgress((int).4 * 100);
        decayTwoSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(decayOneTextView, "ms"));
        decayTwoSeekBar.setEnabled(false);


        //

        final SeekBar delayThreeSeekBar = (SeekBar)view.findViewById(R.id.echo_delay_three_seekbar);
        delayThreeSeekBar.setProgress(0);
        delayThreeSeekBar.setMax(100 * 100);
        delayThreeSeekBar.setProgress(60 * 100);
        delayThreeSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayOneTextView, "ms"));
        delayThreeSeekBar.setEnabled(false);

        final SeekBar decayThreeSeekBar = (SeekBar)view.findViewById(R.id.echo_decay_three_seekbar);
        decayThreeSeekBar.setProgress(0);
        decayThreeSeekBar.setMax(1 * 100);
        decayThreeSeekBar.setProgress((int).4 * 100);
        decayThreeSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(decayOneTextView, "ms"));
        decayThreeSeekBar.setEnabled(false);



        final Switch mainDelayOneSwitch = (Switch)view.findViewById(R.id.echo_delay_group_one_switch);
        mainDelayOneSwitch.setChecked(true);
        mainDelayOneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                delayOneSeekBar.setEnabled(b);
                decayOneSeekBar.setEnabled(b);
            }
        });

        final Switch mainDelayTwoSwitch = (Switch)view.findViewById(R.id.echo_delay_group_two_switch);
        mainDelayTwoSwitch.setChecked(false);
        mainDelayTwoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                delayTwoSeekBar.setEnabled(b);
                decayTwoSeekBar.setEnabled(b);
            }
        });

        final Switch mainDelayThreeSwitch = (Switch)view.findViewById(R.id.echo_delay_group_three_switch);
        mainDelayThreeSwitch.setChecked(false);
        mainDelayThreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                delayThreeSeekBar.setEnabled(b);
                decayThreeSeekBar.setEnabled(b);
            }
        });


        Button resetButton = (Button)view.findViewById(R.id.echo_defaults_button);
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainDelayOneSwitch.setChecked(true);
                mainDelayTwoSwitch.setChecked(false);
                mainDelayThreeSwitch.setChecked(false);

                gainInSeekBar.setProgress(0);
                gainInSeekBar.setProgress((int).5 * 100);

                gainOutSeekBar.setProgress(0);
                gainOutSeekBar.setProgress((int).5 * 100);

                delayOneSeekBar.setEnabled(true);
                delayOneSeekBar.setProgress(0);
                delayOneSeekBar.setProgress(60 * 100);

                decayOneSeekBar.setEnabled(true);
                decayOneSeekBar.setProgress(0);
                decayOneSeekBar.setProgress((int).4 * 100);


                delayTwoSeekBar.setEnabled(true);
                delayTwoSeekBar.setProgress(0);
                delayTwoSeekBar.setProgress(60 * 100);

                decayTwoSeekBar.setEnabled(true);
                decayTwoSeekBar.setProgress(0);
                decayTwoSeekBar.setProgress((int).4 * 100);


                delayThreeSeekBar.setEnabled(true);
                delayThreeSeekBar.setProgress(0);
                delayThreeSeekBar.setProgress(60 * 100);

                decayThreeSeekBar.setEnabled(true);
                decayThreeSeekBar.setProgress(0);
                decayThreeSeekBar.setProgress((int).4 * 100);
            }
        });

        return view;
    }

    public View setupFlangerView(View view)
    {
        root = (RelativeLayout)view.findViewById(R.id.flanger_root_layout);

        final TextView delayTextView = (TextView)view.findViewById(R.id.flanger_delay_value);
        delayTextView.setText("0ms");

        final TextView depthTextView = (TextView)view.findViewById(R.id.flanger_depth_value);
        depthTextView.setText("2ms");

        final TextView regenTextView = (TextView)view.findViewById(R.id.flanger_regen_value);
        regenTextView.setText("0%");

        final TextView widthTextView = (TextView)view.findViewById(R.id.flanger_width_value);
        widthTextView.setText("71%");

        final TextView speedTextView = (TextView)view.findViewById(R.id.flanger_speed_value);
        speedTextView.setText(".5Hz");

        final TextView phaseTextView = (TextView)view.findViewById(R.id.flanger_phase_value);
        phaseTextView.setText("25%");

        final SeekBar delaySeekBar = (SeekBar)view.findViewById(R.id.flanger_delay_seekbar);
        delaySeekBar.setProgress(0);
        delaySeekBar.setMax(30 * 100);
        delaySeekBar.setProgress(0);
        delaySeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(delayTextView, "ms"));

        final SeekBar depthSeekBar = (SeekBar)view.findViewById(R.id.flanger_depth_seekbar);
        depthSeekBar.setProgress(0);
        depthSeekBar.setMax(10 * 100);
        depthSeekBar.setProgress(2 * 100);
        depthSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(depthTextView, "ms"));

        final SeekBar regenSeekBar = (SeekBar)view.findViewById(R.id.flanger_regen_seekbar);
        regenSeekBar.setProgress(0);
        regenSeekBar.setMax(95 * 100);
        regenSeekBar.setProgress(0);
        regenSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(regenTextView, "%"));

        final SeekBar widthSeekBar = (SeekBar)view.findViewById(R.id.flanger_width_seekbar);
        widthSeekBar.setProgress(0);
        widthSeekBar.setMax(100 * 100);
        widthSeekBar.setProgress(71 * 100);
        widthSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(widthTextView, "%"));

        final SeekBar speedSeekBar = (SeekBar)view.findViewById(R.id.flanger_speed_seekbar);
        speedSeekBar.setProgress(0);
        speedSeekBar.setMax(5 * 100);
        speedSeekBar.setProgress((int).5 * 100);
        speedSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(speedTextView, "Hz"));

        final SeekBar phaseSeekBar = (SeekBar)view.findViewById(R.id.flanger_phase_seekbar);
        phaseSeekBar.setProgress(0);
        phaseSeekBar.setMax(100 * 100);
        phaseSeekBar.setProgress(25 * 100);
        phaseSeekBar.setOnSeekBarChangeListener(new SeekbarEffectListener(phaseTextView, "%"));

        final Switch shapeSwitch = (Switch)view.findViewById(R.id.flanger_shape_switch);

        final Switch interpSwitch = (Switch)view.findViewById(R.id.flanger_interep_switch);

        Button resetButton = (Button)view.findViewById(R.id.flanger_reset_button);
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                delayTextView.setText("0ms");
                phaseTextView.setText("25%");
                speedTextView.setText(".5Hz");
                widthTextView.setText("71%");
                regenTextView.setText("0%");
                depthTextView.setText("2ms");

                delaySeekBar.setProgress(0);
                delaySeekBar.setProgress(0);

                depthSeekBar.setProgress(0);
                depthSeekBar.setProgress(2 * 100);

                regenSeekBar.setProgress(0);
                regenSeekBar.setProgress(0);

                widthSeekBar.setProgress(0);
                widthSeekBar.setProgress(71 * 100);

                speedSeekBar.setProgress(0);
                speedSeekBar.setProgress((int).5 * 100);

                phaseSeekBar.setProgress(0);
                phaseSeekBar.setProgress(25 * 100);

                shapeSwitch.setChecked(false);

                interpSwitch.setChecked(false);
            }
        });




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
