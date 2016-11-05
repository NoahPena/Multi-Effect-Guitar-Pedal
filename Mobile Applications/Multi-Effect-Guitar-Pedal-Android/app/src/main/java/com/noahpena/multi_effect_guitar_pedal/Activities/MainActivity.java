package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

import org.w3c.dom.Text;

public class MainActivity extends Activity
{

    RelativeLayout effectsButton;
    ImageView effectsImage;
    TextView effectsText;

    RelativeLayout setlistButton;
    ImageView setlistImage;
    TextView setlistText;

    Drawable graphicImage;
    Drawable graphicGlowImage;
    Drawable guitarImage;
    Drawable guitarGlowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        effectsButton = (RelativeLayout)findViewById(R.id.mainmenuEffectsButton);
        setlistButton = (RelativeLayout)findViewById(R.id.mainmenuSetlistButton);

        effectsImage = (ImageView)findViewById(R.id.mainmenuEffectsImage);
        setlistImage = (ImageView)findViewById(R.id.mainmenuSetlistImage);

        effectsText = (TextView)findViewById(R.id.mainmenuEffectsText);
        setlistText = (TextView)findViewById(R.id.mainmenuSetlistText);

        graphicImage = getResources().getDrawable(R.drawable.graphic_equalizer, null);
        graphicGlowImage = getResources().getDrawable(R.drawable.graphic_equalizer_glow, null);

//        effectsButton.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//
//                if(event.getAction() == MotionEvent.ACTION_DOWN)
//                {
//                    effectsImage.setBackground(graphicGlowImage);
//                }
//                else if(event.getAction() == MotionEvent.ACTION_UP)
//                {
//                    effectsImage.setBackground(graphicImage);
//                }
//
//                return false;
//            }
//        });
    }
}
