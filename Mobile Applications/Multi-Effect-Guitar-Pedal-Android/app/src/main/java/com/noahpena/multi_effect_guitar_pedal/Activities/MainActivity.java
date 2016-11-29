package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.app.Activity;
import android.content.Intent;
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

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        effectsButton = (RelativeLayout)findViewById(R.id.mainmenuEffectsButton);
        setlistButton = (RelativeLayout)findViewById(R.id.mainmenuSetlistButton);

//        effectsImage = (ImageView)findViewById(R.id.mainmenuEffectsImage);
//        setlistImage = (ImageView)findViewById(R.id.mainmenuSetlistImage);

        effectsText = (TextView)findViewById(R.id.mainmenuEffectsText);
        setlistText = (TextView)findViewById(R.id.mainmenuSetlistText);


        effectsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, EffectsActivity.class);
                startActivity(intent);
            }
        });
    }
}
