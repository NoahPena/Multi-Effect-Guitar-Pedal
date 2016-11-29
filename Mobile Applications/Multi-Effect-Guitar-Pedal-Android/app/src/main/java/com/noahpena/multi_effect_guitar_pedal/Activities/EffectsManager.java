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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.noahpena.multi_effect_guitar_pedal.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by noahpena on 11/28/16.
 */

public class EffectsManager
{

    public static UserEffect currentEffect = null;

    private static File appDir;

    public static void init(Context context)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        appDir = new File(root + "/Multi-Effect Guitar Pedal");
        appDir.mkdirs();
    }

    public static void saveEffect(final Activity activity, final BaseEffect effectOne, final BaseEffect effectTwo, final BaseEffect effectThree)
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

                            currentEffect = new UserEffect(editText.getText().toString(), effectOne, effectTwo, effectThree);

                            try
                            {
                                FileOutputStream fos = new FileOutputStream(new File(appDir + "/" + editText.getText().toString() + ".dat"));
                                //FileOutputStream fos = activity.getApplicationContext().openFileOutput(appDir + "/" + editText.getText().toString() + ".dat", Context.MODE_PRIVATE);
                                ObjectOutputStream os = new ObjectOutputStream(fos);

                                currentEffect.setFileName(currentEffect.getName());

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
