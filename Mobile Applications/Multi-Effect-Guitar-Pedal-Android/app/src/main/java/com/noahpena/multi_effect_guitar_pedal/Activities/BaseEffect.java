package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
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
        public int sliderValue = -1;
        public boolean switchValue = false;
        public boolean modulation = false;
        public boolean delay = false;

        public EffectDuple(int elementID, int sliderValue, boolean switchValue, boolean modulation, boolean delay)
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
        }
    }

    private String name;
    private int spinnerPosition;
    private List<EffectDuple> parameters;

    private String command;

    public BaseEffect(String name, int spinnerPosition, RelativeLayout root)
    {
        this.name = name;
        this.spinnerPosition = spinnerPosition;

        createParameters(root);
    }

    public String getCommand()
    {
        return command;
    }

    public String getName()
    {
        return name;
    }

    public int getSpinnerPosition()
    {
        return this.spinnerPosition;
    }

    public List<EffectDuple> getParameters()
    {
        return parameters;
    }

    public void createParameters(RelativeLayout root)
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
                    parameters.add(new EffectDuple(v.getId(), ((SeekBar)v).getProgress(), false, false, false));
                }
                else if(v instanceof Switch)
                {
                    if(((String)v.getTag()).equalsIgnoreCase("modulation"))
                    {
                        parameters.add(new EffectDuple(v.getId(), -1, ((Switch)v).isChecked(), true, false));
                    }
                    else if(((String)v.getTag()).equalsIgnoreCase("delayRoot"))
                    {
                        parameters.add(new EffectDuple(v.getId(), -1, ((Switch)v).isChecked(), false, true));
                    }
                    else
                    {
                        parameters.add(new EffectDuple(v.getId(), -1, ((Switch)v).isChecked(), false , false));
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
                else if(temp.switchValue && temp.modulation)
                {
                    co += " " + "-s";
                }
                else if(!temp.switchValue && temp.modulation)
                {
                    co += " " + "-t";
                }
                else
                {

                }
            }
        }

        this.command = co;
    }
}
