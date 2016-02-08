package com.legend.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Lody
 * @version 1.0
 *
 * Test Hook (Dalvik & Art)
 *
 */
public class MainActivity extends Activity {


    private Button button;
    private Button button2;
    private Button button3;
    private CheckBox enableToastCheckBox;
    private CheckBox enableActivityCheckBox;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        enableToastCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                App.ENABLE_TOAST = isChecked;
            }
        });
        enableActivityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                App.ALLOW_LAUNCH_ACTIVITY = isChecked;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                telephonyManager.getSimSerialNumber();
                String result = telephonyManager.getSimSerialNumber();
                textView.setText("SimSerialNumber = " + result);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello world!", Toast.LENGTH_SHORT).show();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
    }

    private void findViews() {
        enableToastCheckBox = (CheckBox) findViewById(R.id.enable_toast);
        enableActivityCheckBox = (CheckBox) findViewById(R.id.allow_launch_activity);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.text);
        button3 = (Button) findViewById(R.id.startActivity);
    }

}
