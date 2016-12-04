package com.noahpena.multi_effect_guitar_pedal.Activities;

import java.io.Serializable;

/**
 * Created by noahpena on 11/28/16.
 */

public class UserEffect implements Serializable
{

    private String fileName;
    private String name;
    private BaseEffect effectOne;
    private BaseEffect effectTwo;
    private BaseEffect effectThree;
    private String command;


    public UserEffect(String name, BaseEffect effectOne, BaseEffect effectTwo, BaseEffect effectThree)
    {
        this.name = name;
        this.effectOne = effectOne;
        this.effectTwo = effectTwo;
        this.effectThree = effectThree;

        generateCommand();
    }

    public void generateCommand()
    {
        command = effectOne.getCommand();

        if(effectTwo != null)
        {
            command += " " + effectTwo.getCommand();
        }

        if(effectThree != null)
        {
            command += " " + effectThree.getCommand();
        }
    }

    public String getCommand()
    {
        return command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseEffect getEffectOne() {
        return effectOne;
    }

    public void setEffectOne(BaseEffect effectOne) {
        this.effectOne = effectOne;
        generateCommand();
    }

    public BaseEffect getEffectTwo() {
        return effectTwo;
    }

    public void setEffectTwo(BaseEffect effectTwo) {
        this.effectTwo = effectTwo;
        generateCommand();
    }

    public BaseEffect getEffectThree() {
        return effectThree;
    }

    public void setEffectThree(BaseEffect effectThree) {
        this.effectThree = effectThree;
        generateCommand();
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

}
