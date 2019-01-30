package com.example.chessclock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ChooseType extends Screen {

    void butProceedClick() {
        Button butProceed = (Button) findViewById(R.id.butProceed);
        butProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean showMov = false;
                boolean showTot = false;

                if(((RadioButton)(findViewById(R.id.rbTotAndMov))).isChecked()) {
                    showTot = true;
                    showMov = true;
                } else if(((RadioButton)(findViewById(R.id.rbOnlyTot))).isChecked()) {
                    showTot = true;
                    showMov = false;
                } else if (((RadioButton)(findViewById(R.id.rbOnlyMov))).isChecked()) {
                    showTot = false;
                    showMov = true;
                } else {
                    // nothing was chosen
                    createErrorDialog("Choose one option");
                    return;
                }

                Intent it = new Intent(ChooseType.this, OptionsOfType.class);
                it.putExtra("showTot", showTot);
                it.putExtra("showMov", showMov);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        butProceedClick();

    }
}
