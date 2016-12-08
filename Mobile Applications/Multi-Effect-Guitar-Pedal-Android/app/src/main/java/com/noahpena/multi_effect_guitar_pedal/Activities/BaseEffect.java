package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by noahpena on 11/28/16.
 */

public class BaseEffect implements Serializable
{

    public static class EffectDuple implements Serializable
    {
        public int elementID;
        public double sliderValue = -1;
        public boolean switchValue = false;
        public boolean modulation = false;
        public boolean interpolation = false;
        public boolean shape = false;
        public boolean delay = false;

        public EffectDuple(int elementID, int sliderValue, boolean switchValue, boolean modulation, boolean delay, boolean interpolation, boolean shape)
        {
            this.elementID = elementID;

            if(sliderValue != -1)
            {
                this.sliderValue = sliderValue;
            }
            else
            {
                this.switchValue = switchValue;
            }

            this.modulation = modulation;
            this.delay = delay;
            this.interpolation = interpolation;
            this.shape = shape;
        }
    }

    private String tabName;
    private String name;
    private int spinnerPosition;
    private List<EffectDuple> parameters;

    private String command;

    public BaseEffect(String name, int spinnerPosition, EffectsFragment fragment)
    {
        this.tabName = name;
        this.name = name.toLowerCase().replaceAll("\\s+", "");
        this.spinnerPosition = spinnerPosition;

        createParameters(fragment.root, fragment.fragmentView);
    }

    public String getCommand()
    {
        return command;
    }

    public String getName()
    {
        return name;
    }

    public String getTabName()
    {
        return tabName;
    }

    public int getSpinnerPosition()
    {
        return this.spinnerPosition;
    }

    public List<EffectDuple> getParameters()
    {
        return parameters;
    }

    public void createParameters(RelativeLayout root, View parent)
    {
        List<EffectDuple> parameters = new ArrayList<>();

        final int childCount = root.getChildCount();

        for(int i = 0; i < childCount; i++)
        {
            View v = root.getChildAt(i);

            if(v.isEnabled())
            {
                if(v instanceof SeekBar)
                {
                    SeekBar temp = (SeekBar)parent.findViewById(v.getId());

                    int progress = temp.getProgress();

                    if(temp.getTag() == "speed" && progress < 10)
                    {
                        progress = 10;
                    }
                    else if(temp.getTag() == "chorusDelay" && progress < 20)
                    {
                        progress = 20;
                    }

                    parameters.add(new EffectDuple(v.getId(), progress, false, false, false, false, false));
                }
                else if(v instanceof Switch)
                {
                    if(((String)v.getTag()).equalsIgnoreCase("modulation"))
                    {
                        Switch temp = (Switch)parent.findViewById(v.getId());

                        parameters.add(new EffectDuple(v.getId(), -1, temp.isChecked(), true, false, false, false));
                    }
                    else if(((String)v.getTag()).equalsIgnoreCase("delayRoot"))
                    {
                        Switch temp = (Switch)parent.findViewById(v.getId());

                        parameters.add(new EffectDuple(v.getId(), -1, temp.isChecked(), false, true, false, false));
                    }
                    else if(((String)v.getTag()).equalsIgnoreCase("interpolation"))
                    {
                        Switch temp = (Switch)parent.findViewById(v.getId());

                        parameters.add(new EffectDuple(v.getId(), -1, temp.isChecked(), false, false, true, false));
                    }
                    else if(((String)v.getTag()).equalsIgnoreCase("shape"))
                    {
                        Switch temp = (Switch)parent.findViewById(v.getId());

                        parameters.add(new EffectDuple(v.getId(), -1, temp.isChecked(), false, false, false, true));
                    }
                    else
                    {
                        Switch temp = (Switch)parent.findViewById(v.getId());

                        parameters.add(new EffectDuple(v.getId(), -1, temp.isChecked(), false , false, false, false));
                    }
                }
            }
        }

        this.parameters = parameters;

        generateCommand();
    }

    private void generateCommand()
    {
        String co = name;

        for(int i = 0; i < parameters.size(); i++)
        {
            EffectDuple temp = parameters.get(i);

            if(temp.sliderValue != -1)
            {
                co += " " + Double.toString((double)temp.sliderValue/100);
            }
            else
            {
                if(temp.switchValue && temp.delay)
                {

                }
                else if(!temp.switchValue && temp.modulation)
                {
                    co += " " + "-s";
                }
                else if(temp.switchValue && temp.modulation)
                {
                    co += " " + "-t";
                }
                else if(temp.switchValue && temp.interpolation)
                {
                    co += " " + "qua";
                }
                else if(!temp.switchValue && temp.interpolation)
                {
                    co += " " + "lin";
                }
                else if(temp.switchValue && temp.shape)
                {
                    co += " " + "tri";
                }
                else if(!temp.switchValue && temp.shape)
                {
                    co += " " + "sin";
                }
                else
                {

                }
            }
        }

        Log.d("DEBUG", co);

        this.command = co;
    }
}
