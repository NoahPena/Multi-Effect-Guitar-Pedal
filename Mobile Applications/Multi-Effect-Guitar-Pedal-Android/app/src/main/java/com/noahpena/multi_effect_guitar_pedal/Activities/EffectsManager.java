package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahpena on 11/28/16.
 */

public class EffectsManager
{

    public static UserEffect currentEffect = null;
    public static BaseEffect currentTabOne = null;
    public static BaseEffect currentTabTwo = null;
    public static BaseEffect currentTabThree = null;
    public static boolean updateTabOne = false;
    public static boolean updateTabTwo = false;
    public static boolean updateTabThree = false;

    private static File appDir;

    public static void init(Context context)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        appDir = new File(root + "/Multi-Effect Guitar Pedal");
        appDir.mkdirs();
    }

    public static void openEffect(final Activity activity)
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.myDialog));

        final UserEffect[] temp = new UserEffect[1];

        builder1.setTitle("Select Effect");
        builder1.setCancelable(true);

        final String[] names = appDir.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File file, String s)
            {
                return s.endsWith(".effect");
            }
        });

        builder1.setItems(names, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();

                currentTabTwo = null;
                currentTabThree = null;

                try
                {
                    FileInputStream fis = new FileInputStream(new File(appDir + "/" + names[i]));
                    ObjectInputStream is = new ObjectInputStream(fis);
                    currentEffect = (UserEffect)is.readObject();
                    is.close();
                    fis.close();

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                if(currentEffect != null)
                {

                    Log.d("PLS", "Command: " + currentEffect.getCommand());

                    if(currentEffect.getEffectTwo() != null)
                    {
                        Log.d("PLS", "IZREAL: " + currentEffect.getEffectTwo().getName());
                    }

                    if(currentEffect.getEffectThree() != null)
                    {
                        Log.d("PLS", "URREAL: " + currentEffect.getEffectThree().getName());
                    }

//                    EffectsManager.currentEffect = null;
                    EffectsManager.currentTabOne = currentEffect.getEffectOne();
                    EffectsManager.currentTabTwo = currentEffect.getEffectTwo();
                    EffectsManager.currentTabThree = currentEffect.getEffectThree();

                    updateTabOne = false;
                    updateTabTwo = false;
                    updateTabThree = false;

                    int tabs = 0;
                    String msg = "";

                    if(currentTabOne != null)
                    {
                        UserPreferences.setTabOneEffect(activity.getApplicationContext(), currentTabOne.getTabName(), currentTabOne.getSpinnerPosition());
                        msg = currentEffect.getEffectOne().getName();
                        updateTabOne = true;
                        tabs++;
                    }

                    if(currentTabTwo != null)
                    {
                        UserPreferences.setTabTwoEffect(activity.getApplicationContext(), currentTabTwo.getTabName(), currentTabTwo.getSpinnerPosition());
                        updateTabTwo = true;
                        msg += ", " + currentEffect.getEffectTwo().getName();
                        tabs++;
                    }

                    if(currentTabThree != null)
                    {
                        UserPreferences.setTabThreeEffect(activity.getApplicationContext(), currentTabThree.getTabName(), currentTabThree.getSpinnerPosition());
                        updateTabThree = true;
                        msg += ", " + currentEffect.getEffectThree().getName();
                        tabs++;
                    }



//                    EffectsManager.currentEffect = null;
//                    EffectsManager.currentTabOne = null;
//                    EffectsManager.currentTabTwo = null;
//                    EffectsManager.currentTabThree = null;

                    Log.d("DEBUG", "Opened Tabs: " + tabs);
                    Log.d("DEBUG", "Opened Effects: " + msg);

                    ((TextView)activity.findViewById(R.id.userEffectNameTextBox)).setText(currentEffect.getName());



                    EffectsActivity.effectsPageAdapter.setAmountOfTabs(tabs);
//                    effectName.setText("");
//                    temp[0] = currentEffect;
//
//                    ((TextView)activity.findViewById(R.id.userEffectNameTextBox)).setText(currentEffect.getName());
//                    currentTabOne = currentEffect.getEffectOne();
//                    currentTabTwo = currentEffect.getEffectTwo();
//                    currentTabThree = currentEffect.getEffectThree();
//
//                    EffectsManager.updateTabOne = true;
//                    EffectsManager.updateTabTwo = true;
//                    EffectsManager.updateTabThree = true;
//
//                    String msg = currentTabOne.getName();
//
//                    if(currentTabTwo != null)
//                    {
//                        msg += ", " + currentTabTwo.getName();
//                    }
//
//                    if(currentTabThree != null)
//                    {
//                        msg += ", " + currentTabThree.getName();
//                    }
//
//                    Log.d("PLS", msg);
//
//                    int tabs = 3;
//
//                    UserPreferences.setTabOneEffect(activity.getApplicationContext(), currentTabOne.getName(), currentTabOne.getSpinnerPosition());
//
//                    if(currentTabThree == null)
//                    {
//                        tabs--;
//                    }
//                    else
//                    {
//                        UserPreferences.setTabThreeEffect(activity.getApplicationContext(), currentTabThree.getName(), currentTabThree.getSpinnerPosition());
//                    }
//                    if(currentTabTwo == null)
//                    {
//                        tabs--;
//                    }
//                    else
//                    {
//                        UserPreferences.setTabTwoEffect(activity.getApplicationContext(), currentTabTwo.getName(), currentTabTwo.getSpinnerPosition());
//                    }
//
//                    EffectsActivity.effectsPageAdapter.setAmountOfTabs(tabs);
//
//                    EffectsManager.currentEffect = temp[0];

                }
            }
        });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void saveOverEffect(final Activity activity)
    {
        currentEffect.setEffectOne(currentTabOne);
        currentEffect.setEffectTwo(currentTabTwo);
        currentEffect.setEffectThree(currentTabThree);

        Log.d("PLS", "SAVED OVER WITH: " + currentEffect.getCommand());

        String msg = currentEffect.getEffectOne().getName();

        if(currentTabTwo != null)
        {
            msg += ", " + currentEffect.getEffectTwo().getName();
        }

        if(currentTabThree != null)
        {
            msg += ", " + currentEffect.getEffectThree().getName();
        }

        Log.d("DEBUG", "Tabs: " + msg);

        try
        {
            new File(appDir + "/" + currentEffect.getFileName()).delete();
            FileOutputStream fos = new FileOutputStream(new File(appDir + "/" + currentEffect.getFileName()));
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(currentEffect);
            os.close();
            fos.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveNewEffect(final Activity activity)
    {

        if(currentEffect == null)
        {
            //First Time Saving

            AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.myDialog));

            final LayoutInflater inflater = activity.getLayoutInflater();

            final View view = inflater.inflate(R.layout.dialog_ask_name, null);

            builder1.setMessage("Enter Custom Effect Name");
            builder1.setCancelable(true);

            builder1.setView(view);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.cancel();

                            EditText editText = (EditText)view.findViewById(R.id.custom_effect_name_textbox);

                            currentEffect = new UserEffect(editText.getText().toString(), currentTabOne, currentTabTwo, currentTabThree);

                            Log.d("PLS", "JUST CREATED: " + currentEffect.getCommand());

                            try
                            {
                                FileOutputStream fos = new FileOutputStream(new File(appDir + "/" + editText.getText().toString() + ".effect"));
                                //FileOutputStream fos = activity.getApplicationContext().openFileOutput(appDir + "/" + editText.getText().toString() + ".dat", Context.MODE_PRIVATE);
                                ObjectOutputStream os = new ObjectOutputStream(fos);

                                currentEffect.setFileName(currentEffect.getName() + ".effect");

                                os.writeObject(currentEffect);
                                os.close();
                                fos.close();
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }


}
