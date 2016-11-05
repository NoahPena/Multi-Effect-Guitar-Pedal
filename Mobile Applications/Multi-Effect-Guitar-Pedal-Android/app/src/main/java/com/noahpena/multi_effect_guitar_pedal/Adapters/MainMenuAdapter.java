package com.noahpena.multi_effect_guitar_pedal.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

/**
 * Created by noahpena on 11/2/16.
 */

public class MainMenuAdapter extends BaseAdapter
{

    private String itemOneText = "Create/Edit Effects";
    private String itemTwoText = "Create/Play Set List";

    private int[] itemOne = {R.drawable.graphic_equalizer, R.drawable.graphic_equalizer_glow};
    private int[] itemTwo = {R.drawable.electric_guitar_outline, R.drawable.electric_guitar_outline_glow};

    private Activity mActivity;
    private LayoutInflater inflater;

    public MainMenuAdapter(Activity activity)
    {
        this.mActivity = activity;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        inflater = mActivity.getLayoutInflater();

        View row = inflater.inflate(R.layout.mainmenu_item, null);

        final ImageView imageView = (ImageView) row.findViewById(R.id.mainMenuItemImage);
        TextView textView = (TextView) row.findViewById(R.id.mainMenuItemText);

        if(i == 0)
        {
            imageView.setImageResource(itemOne[0]);
            textView.setText(itemOneText);
        }
        else
        {
            imageView.setImageResource(itemTwo[0]);
            textView.setText(itemTwoText);
        }

        row.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(i == 0)
                    {
                        imageView.setImageResource(itemOne[1]);
                    }
                    else
                    {
                        imageView.setImageResource(itemTwo[1]);
                    }
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(i == 0)
                    {
                        imageView.setImageResource(itemOne[0]);
                    }
                    else
                    {
                        imageView.setImageResource(itemTwo[0]);
                    }
                }

                return false;
            }
        });


        return row;


    }
}
