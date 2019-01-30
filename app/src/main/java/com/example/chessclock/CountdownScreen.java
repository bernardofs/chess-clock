package com.example.chessclock;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountdownScreen extends Screen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_screen);

        final TextView tv = (TextView) findViewById(R.id.tvWhiteMove);
        TextView tv2 = (TextView) findViewById(R.id.tvWhiteTotal);
        ConstraintLayout clWhite = (ConstraintLayout) findViewById(R.id.clWhite);

        final CountDownTimer cdtp = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int x = (int)millisUntilFinished;
                tv.setText(Integer.toString(x / 1000));
            }

            @Override
            public void onFinish() {

            }
        };

        cdtp.start();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cdtp.ismPaused())
                    cdtp.resume();
                else
                    cdtp.pause();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdtp.pause();
            }
        });


        clWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdtp.pause();
            }
        });



    }
}
