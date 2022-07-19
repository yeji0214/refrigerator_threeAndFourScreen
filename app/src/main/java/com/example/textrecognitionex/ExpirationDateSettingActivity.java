package com.example.textrecognitionex;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class ExpirationDateSettingActivity extends AppCompatActivity {
    Switch one_day_ago, three_day_ago, five_day_ago, seven_day_ago;
    Button button_return_from_expiration_date_setting;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiration_date_setting);
        getSupportActionBar().setTitle("알림 설정");

        Intent intent = getIntent();
        color = intent.getExtras().getInt("theme");

        if (color == 1) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFBB86FC));
            getWindow().setStatusBarColor(0xFFA566FF);
        }
        else if (color == 2) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF03DAC5));
            getWindow().setStatusBarColor(0xFF3DB7CC);
        }
        else if (color == 3) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
            getWindow().setStatusBarColor(0xFF4C4C4C);
        }
        else if (color == 4) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB2D9));
            getWindow().setStatusBarColor(0xFFEDA0C7);
        }

        one_day_ago = findViewById(R.id.one_day_ago_alarm_switch);
        three_day_ago = findViewById(R.id.three_day_ago_alarm_switch);
        five_day_ago = findViewById(R.id.five_day_ago_alarm_switch);
        seven_day_ago = findViewById(R.id.seven_day_ago_alarm_switch);
        button_return_from_expiration_date_setting = findViewById(R.id.button_return_from_expiration_date_setting);

        one_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    one_day_ago.setText("1일 전 알람 켜짐");
                }else {
                    one_day_ago.setText("1일 전 알람 꺼짐");
                }
            }
        });

        three_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    three_day_ago.setText("3일 전 알람 켜짐");
                }else {
                    three_day_ago.setText("3일 전 알람 꺼짐");
                }
            }
        });

        five_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    five_day_ago.setText("5일 전 알람 켜짐");
                }else {
                    five_day_ago.setText("5일 전 알람 꺼짐");
                }
            }
        });

        seven_day_ago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    seven_day_ago.setText("7일 전 알람 켜짐");
                }else {
                    seven_day_ago.setText("7일 전 알람 꺼짐");
                }
            }
        });

        button_return_from_expiration_date_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
