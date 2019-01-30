package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

public class OptionsOfType extends Screen {

    private boolean showTot;
    private boolean showMov;

    void getFromLastActivity() {
        showTot = getBoolVarFromLastActivity("showTot");
        showMov = getBoolVarFromLastActivity("showMov");
    }

    void hideElements() {

        if(showTot == false) {
            ((Space)findViewById(R.id.space1)).setVisibility(View.GONE);
            ((Space)findViewById(R.id.space2)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.tvSetTotalTime)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.listTot)).setVisibility(View.GONE);
        }

        if(showMov == false) {
            ((Space)findViewById(R.id.space2)).setVisibility(View.GONE);
            ((Space)findViewById(R.id.space3)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvSetIndividualTime)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.listMov)).setVisibility(View.GONE);
        }
    }

    void butProceedClick() {

        Button butProceed = (Button) findViewById(R.id.butProceed);
        butProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int timeTotal = 0;
                int timeMov = 0;

                if(showTot) {

                    EditText et = ((EditText)findViewById(R.id.etSetTotalTime));

                    if(checkETisValidPositive(et)) {
                        timeTotal = Integer.parseInt(et.getText().toString());
                    } else {
                        return;
                    }

                }

                if(showMov) {
                    EditText et = ((EditText)findViewById(R.id.etSetIndividualTime));

                    if(checkETisValidPositive(et)) {
                        timeMov = Integer.parseInt(et.getText().toString());
                    } else {
                        return;
                    }
                }
                Intent it = new Intent(OptionsOfType.this, CountdownScreen.class);
                it.putExtra("showTot", showTot);
                it.putExtra("showMov", showMov);
                it.putExtra("timeTotal", timeTotal);
                it.putExtra("timeMov", timeMov);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_of_type);

        getFromLastActivity();
        hideElements();

        butProceedClick();
    }
}
