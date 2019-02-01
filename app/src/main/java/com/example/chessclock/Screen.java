package com.example.chessclock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class Screen extends AppCompatActivity {

    // function
    void changeActivityAndKeep(Context packageContext, Class cls) {
        Intent it = new Intent(packageContext, cls);
        startActivity(it);
    }

    int getIntVarFromLastActivity(String name) {
        // this function returns the variable value stored from the last activity
        return getIntent().getIntExtra(name, -1);
    }

    boolean getBoolVarFromLastActivity(String name) {
        // this function returns the variable value stored from the last activity
        return getIntent().getBooleanExtra(name, false);
    }

    void backToInitialScreenAndCloseAllActivities() {
        Intent intent = new Intent(getApplicationContext(), ChooseType.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    ////////////////////////// Tests ////////////////////////////

    boolean isNonNegative(String x) {
        if(x.charAt(0) != '-') {
            return true;
        } else {
            createErrorDialog("Only non-negative numbers are allowed");
            return false;
        }
    }

    boolean isNumber(String s) {

        if(numberDoNotOverflow(s) == false) {
            createErrorDialog("Number is too big");
            return false;
        }

        if(s.charAt(0) == '-' && s.length() == 1) {
            createErrorDialog("Invalid number");
            return false;
        }

        for(int i = (s.charAt(0) == '-' ? 1 : 0); i < s.length(); i++) {
            if(s.charAt(i) < '0' || s.charAt(i) > '9') {
                createErrorDialog("Invalid number");
                return false;
            }
        }

        return true;
    }

    boolean isNumber(int x) {
        return isNumber(Integer.toString(x));
    }

    boolean isEmpty(String x) {
        if(x.length() == 0) {
            createErrorDialog("Some required field is empty");
            return true;
        } else
            return false;
    }

    boolean numberDoNotOverflow(String x) {
        if(x.charAt(0) == '-') {
            x = x.substring(1);
        }

        if(x.equals("10000000") || x.length() < 8)
            return true;

        return false;
    }

    boolean isNotZero(String x) {
        if (Integer.parseInt(x) != 0)
            return true;
        else {
            createErrorDialog("Time must be greater than zero");
            return false;
        }
    }

    boolean checkETisValidNonNegativeNumber(EditText et) {
        String text = et.getText().toString();
        return !isEmpty(text) && isNumber(text) && isNonNegative(text);
    }

    boolean checkETisValidPositive(EditText et) {
        String text = et.getText().toString();
        return !isEmpty(text) && isNumber(text) && isNotZero(text);
    }

    boolean checkETisValidNumber(EditText et) {
        String text = et.getText().toString();
        return !isEmpty(text) && isNumber(text);
    }

    ////////////////////////// Tests ////////////////////////////

    void createErrorDialog(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error");
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }

    void createDialog(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }


}
