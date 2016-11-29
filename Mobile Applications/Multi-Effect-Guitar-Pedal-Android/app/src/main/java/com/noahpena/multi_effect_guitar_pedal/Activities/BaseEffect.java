package com.noahpena.multi_effect_guitar_pedal.Activities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by noahpena on 11/28/16.
 */

public class BaseEffect implements Serializable
{

    private String name;
    private List<Object> parameters;

    public BaseEffect(String name, List<Object> parameters)
    {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName()
    {
        return name;
    }

    public List<Object> getParameters()
    {
        return parameters;
    }
}
