package com.example.sagcalculation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        Bundle a = getIntent().getExtras();
        double result = a.getDouble("provjes");
        double result1 = a.getDouble("provjes1");
        double result2 = a.getDouble("provjes2");

        DecimalFormat df = new DecimalFormat("0.00");


        TextView t7 = (TextView) findViewById(R.id.textView7);
        t7.setText("Provjes na -20°C je " + (df.format(result)) + " m");
        TextView t8 = (TextView) findViewById(R.id.textView8);
        t8.setText("Provjes na -5°C sa dodatnim teretom je " + (df.format(result1)) + " m");
        TextView t9 = (TextView) findViewById(R.id.textView9);
        t9.setText("Provjes na 40°C je " + (df.format(result2)) + " m");
    }
}
